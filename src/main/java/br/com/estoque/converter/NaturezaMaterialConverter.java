package br.com.estoque.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import br.com.estoque.model.NaturezaMaterial;

import org.omnifaces.converter.SelectItemsConverter;

@FacesConverter("naturezaMaterialConverter")
public class NaturezaMaterialConverter extends SelectItemsConverter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Long id = (value instanceof NaturezaMaterial) ? ((NaturezaMaterial) value).getId() : null;
        return (id != null) ? String.valueOf(id) : null;
    }

}
