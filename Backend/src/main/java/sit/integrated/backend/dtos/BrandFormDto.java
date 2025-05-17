package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class BrandFormDto {
    private Integer id;
    private String name;
    private String websiteUrl;
    private String countryOfOrigin;
    private Boolean isActive;

    public void setName(String name) {
        if (name == null || name.trim().isBlank())
            this.name = null;
        else
            this.name = name.trim();
    }
}
