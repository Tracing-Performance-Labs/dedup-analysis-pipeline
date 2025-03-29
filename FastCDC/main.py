from fastcdc.fastcdc_py import fastcdc_py
from kafka import KafkaConsumer, KafkaProducer
import sys, os, time
import hashlib
from dataclasses import dataclass

@dataclass
class Config:
    input_topic: str
    output_topic: str
    broker_addr: str


def hash(contents: bytes | str) -> str:
    """Hash contents using MD5."""
    md5 = hashlib.md5()
    if isinstance(contents, bytes):
        md5.update(contents)
    else:
        md5.update(contents.encode('utf-8'))
    return md5.hexdigest()


def _main(config: Config):
    consumer = KafkaConsumer(config.input_topic,
        bootstrap_servers=config.broker_addr,
        group_id='dedupe-pipeline-fastcdc-' + str(time.time()),
        key_deserializer=lambda k: None,
        value_deserializer=lambda v: v.decode('utf-8') if v else None)

    producer = KafkaProducer(
        bootstrap_servers=config.broker_addr,
        key_serializer=lambda k: None,
        value_serializer=lambda v: v.encode('utf-8') if v else None)

    spans = set()

    for record in consumer:
        data = record.value
        if data is None:
            continue

        spans.add(data)

        if len(spans) > 100:
            data = '\n'.join(spans)
            chunks = fastcdc_py(bytearray(data, encoding='UTF-8'), fat=True)
            spans.clear()

            for chunk in chunks:
                hsh = hash(chunk.data)
                producer.send(config.output_topic, value=hsh)


if __name__ == '__main__':
    input_topic = os.getenv('INGESTION_TOPIC')
    if input_topic is None:
        print('Missisng input topic')
        sys.exit(1)

    output_topic = os.getenv('FASTCDC_OUTPUT_TOPIC')
    if output_topic is None:
        print('Missing output topic')
        sys.exit(1)

    broker_addr = os.getenv('KAFKA_ADDR')
    if broker_addr is None:
        print('Missing broker address')
        sys.exit(1)

    config = Config(input_topic, output_topic, broker_addr)

    _main(config)
