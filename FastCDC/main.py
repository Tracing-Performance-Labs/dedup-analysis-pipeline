from os import getenv
from fastcdc.fastcdc_py import fastcdc_py
from kafka import KafkaConsumer
import sys, os, time


def _main(broker_addr: str, topic: str):
    consumer = KafkaConsumer(topic,
        bootstrap_servers=broker_addr,
        group_id='dedupe-pipeline-fastcdc-' + str(time.time()),
        key_deserializer=lambda k: None,
        value_deserializer=lambda v: v.decode('utf-8') if v else None)

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
                # TODO: Hash each chunk with MD5
                # TODO: Send each chunk hash to an output topic
                print(chunk.offset)


if __name__ == '__main__':
    topic = os.getenv('INGESTION_TOPIC')
    if topic is None:
        print('Missisng input topic')
        sys.exit(1)

    broker_addr = os.getenv('KAFKA_ADDR')
    if broker_addr is None:
        print('Missing broker address')
        sys.exit(1)

    _main(broker_addr, topic)
