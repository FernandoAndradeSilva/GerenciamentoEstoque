package br.com.estoque.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import br.com.estoque.model.Categoria;

import org.omnifaces.converter.SelectItemsConverter;

@FacesConverter("categoriaConverter")
public class CategoriaConverter extends SelectItemsConverter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Long id = (value instanceof Categoria) ? ((Categoria) value).getId() : null;
        return (id != null) ? String.valueOf(id) : null;
    }

}
