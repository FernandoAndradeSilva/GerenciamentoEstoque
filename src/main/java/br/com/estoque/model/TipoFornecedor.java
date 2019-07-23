package br.com.estoque.model;

public enum TipoFornecedor {

        PessoaFisica("Pessoa-Fisica"),
        PessoaJuridica("Pessoa-Juridica"),
        Outros("Outros");




        private String tipoFornecedor;

        TipoFornecedor(String tipoFornecedor) {
            this.tipoFornecedor = tipoFornecedor;
        }

        public String getTipoFornecedor() {
            return tipoFornecedor;
        }


}
