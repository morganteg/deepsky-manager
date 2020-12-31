package it.attsd.deepsky.dto;

public class DeepSkyObjectDto {
    private String name;
    private ConstellationDto constellation;

    public DeepSkyObjectDto(String name, ConstellationDto constellation) {
        this.name = name;
        this.constellation = constellation;
    }

    public String getName() {
        return name;
    }

    public ConstellationDto getConstellation() {
        return constellation;
    }

}
