package it.attsd.deepsky.dto;

public class ConstellationDto {
    private Long id;
    private String name;

    public ConstellationDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
