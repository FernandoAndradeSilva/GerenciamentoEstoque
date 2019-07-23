package br.com.estoque.converter;


import br.com.estoque.model.UnidadeDeMedida;
import org.omnifaces.converter.SelectItemsConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

@FacesConverter("unidadeDeMedidaConverter")
public class UnidadeDeMedidaConverter extends SelectItemsConverter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Long id = (value instanceof UnidadeDeMedida) ? ((UnidadeDeMedida) value).getId() : null;
        return (id != null) ? String.valueOf(id) : null;
    }

}
