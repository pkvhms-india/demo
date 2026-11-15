package com.varicent.common.port.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PortSourceResolver {
	Map<String, String> m_portSourceTableMap;
	Map<String, Map<String, String>> m_portSourceColumnMap;
	static Map<Collection<Port>, Map<String, String>> portSourceTableMapCache = new ConcurrentHashMap();
	static Map<Collection<Port>, Map<String, Map<String, String>>> portSourceColumnMapCache = new ConcurrentHashMap();

	public PortSourceResolver(Collection<Port> allPorts) {
		if (portSourceTableMapCache.containsKey(allPorts)) {
			this.m_portSourceTableMap = portSourceTableMapCache.get(allPorts);
			this.m_portSourceColumnMap = portSourceColumnMapCache.get(allPorts);
		} else {
			this.m_portSourceTableMap = new HashMap(allPorts.size());
			this.m_portSourceColumnMap = new ConcurrentHashMap(allPorts.size());
			this.initializeResolver(allPorts);
		}

	}

	synchronized void initializeResolver(Collection<Port> allPorts) {
		Map<String, String> tableMap = new HashMap(allPorts.size());
		Map<String, Map<String, String>> columnMap = new ConcurrentHashMap(allPorts.size());

		for (Port port : allPorts) {
			String outputTable = port.getOutputTable();
			String sourceTable = port.getSourceTable();
			tableMap.put(outputTable, sourceTable);
			Map<String, String> singlePortColumnMap = new HashMap();

			for (PortColumn column : port.getColumns()) {
				singlePortColumnMap.put(column.getName(), column.getSourceColumn());
			}

			columnMap.put(outputTable, singlePortColumnMap);
		}

		for (Port port : allPorts) {
			String outputTable = port.getOutputTable();
			String sourceTable = port.getSourceTable();

			Map<String, String> firstColumnMap;
			for (firstColumnMap = columnMap.get(outputTable); sourceTable != null && !sourceTable.isEmpty()
					&& sourceTable.startsWith("_Port"); sourceTable = tableMap.get(sourceTable)) {
				Map<String, String> secondColumnMap = columnMap.get(sourceTable);
				Map<String, String> newColumnMap = new HashMap(firstColumnMap.size());

				for (String outputColumn : firstColumnMap.keySet()) {
					newColumnMap.put(outputColumn, secondColumnMap.get(firstColumnMap.get(outputColumn)));
				}

				firstColumnMap = newColumnMap;
			}

			this.m_portSourceTableMap.put(outputTable, sourceTable);
			this.m_portSourceColumnMap.put(outputTable, firstColumnMap);
		}

		portSourceTableMapCache.put(allPorts, this.m_portSourceTableMap);
		portSourceColumnMapCache.put(allPorts, this.m_portSourceColumnMap);
	}

	public String getSource(String portOutput) {
		return this.m_portSourceTableMap.get(portOutput);
	}

	public String getSourceColumn(String portOutput, String portColumn) {
		return this.isConnected(portOutput) ? (String) this.m_portSourceColumnMap.get(portOutput).get(portColumn)
				: null;
	}

	public Map<String, String> getSourceColumnMap(String portOutput) {
		return this.isConnected(portOutput) ? (Map) this.m_portSourceColumnMap.get(portOutput) : null;
	}

	public boolean isConnected(String portOutput) {
		String sourceTable = this.m_portSourceTableMap.get(portOutput);
		return sourceTable != null && !sourceTable.isEmpty();
	}
}
