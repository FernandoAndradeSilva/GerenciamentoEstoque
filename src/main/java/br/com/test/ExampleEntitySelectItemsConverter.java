package br.com.test;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import br.com.estoque.model.Setor;

import org.omnifaces.converter.SelectItemsConverter;

@FacesConverter("exampleEntitySelectItemsConverter")
public class ExampleEntitySelectItemsConverter extends SelectItemsConverter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Long id = (value instanceof Setor) ? ((Setor) value).getId() : null;
        return (id != null) ? String.valueOf(id) : null;
    }

}
