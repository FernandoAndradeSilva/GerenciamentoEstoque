package br.com.estoque.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import br.com.estoque.model.Unidade;

import org.omnifaces.converter.SelectItemsConverter;

@FacesConverter("unidadeConverter")
public class UnidadeConverter extends SelectItemsConverter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Long id = (value instanceof Unidade) ? ((Unidade) value).getId() : null;
        return (id != null) ? String.valueOf(id) : null;
    }

}
