package titan.ccp.aggregation.teetimebased;

import java.util.concurrent.CompletableFuture;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.Configuration;
import teetime.stage.InstanceOfFilter;
import titan.ccp.model.PowerConsumptionRecord;
import titan.ccp.model.sensorregistry.SensorRegistry;

public class AggregationConfiguration extends Configuration {

	final KafkaReaderStage kafkaReader;

	public AggregationConfiguration(final SensorRegistry sensorRegistry, final SensorHistory sensorHistory) {
		super();
		this.kafkaReader = new KafkaReaderStage();
		final InstanceOfFilter<IMonitoringRecord, PowerConsumptionRecord> instanceOfFilter = new InstanceOfFilter<>(
				PowerConsumptionRecord.class);
		final AggregatorStage aggregator = new AggregatorStage(sensorRegistry, sensorHistory);
		// TODO Storage Stage missing
		super.from(this.kafkaReader).to(instanceOfFilter, s -> s.getInputPort(), s -> s.getMatchedOutputPort())
				.end(aggregator);
	}

	public CompletableFuture<Void> requestTermination() {
		return this.kafkaReader.requestTermination();
	}

}