package com.moises.foodapp.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.moises.foodapp.domain.model.Cozinha;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@JsonRootName("cozinhas")
@Data
public class CozinhasXmlWrapper {

    @JsonProperty("cozinha")
//	@JacksonXmlProperty(localName = "cozinha")
    @JacksonXmlElementWrapper(useWrapping = false)
    @NonNull
    private List<Cozinha> cozinhas;

}
