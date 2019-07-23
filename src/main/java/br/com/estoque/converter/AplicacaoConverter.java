package br.com.estoque.converter;


import br.com.estoque.model.Aplicacao;
import br.com.estoque.model.CentroCusto;
import org.omnifaces.converter.SelectItemsConverter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

@FacesConverter("aplicacaoConverter")
public class AplicacaoConverter extends SelectItemsConverter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Long id = (value instanceof Aplicacao) ? ((Aplicacao) value).getId() : null;
        return (id != null) ? String.valueOf(id) : null;
    }

}
