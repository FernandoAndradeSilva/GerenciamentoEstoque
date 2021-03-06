package br.com.estoque.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import br.com.estoque.model.Fornecedor;

import org.omnifaces.converter.SelectItemsConverter;

@FacesConverter("fornecedorConverter")
public class FornecedorConverter extends SelectItemsConverter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Long id = (value instanceof Fornecedor) ? ((Fornecedor) value).getId() : null;
        return (id != null) ? String.valueOf(id) : null;
    }

}
