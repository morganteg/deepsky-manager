package it.attsd.deepsky.pojo.deepskyobject;

public class DeepSkyObjectSaveRequest {
	private String name;
	
	private long constellationId;
	
	private long deepSkyObjectTypeId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getConstellationId() {
		return constellationId;
	}

	public void setConstellationId(long constellationId) {
		this.constellationId = constellationId;
	}

	public long getDeepSkyObjectTypeId() {
		return deepSkyObjectTypeId;
	}

	public void setDeepSkyObjectTypeId(long deepSkyObjectTypeId) {
		this.deepSkyObjectTypeId = deepSkyObjectTypeId;
	}
	
}