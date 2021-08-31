# Separador e Renomeador de PDFs para Contabilidade

## Objetivo

Aplicação para auxiliar no processo de dividir e renomear pdfs de boletos e notas fiscais.

Ao gerar boletos de cobrança para clientes, é comum que o banco gere **um único arquivo PDF com
todos os boletos de todos os clientes**. O mesmo vale para a emissão de **Notas Fiscais
Eletrônicas** da prefeitura.

Esse programa lê um arquivo pdf contendo vários boletos de vários clientes e o separa, salvando cada
arquivo pdf na pasta correspondente ao mês atual com uma pasta para cada cliente e o arquivo pdf
fica com o nome do cliente correspondente, adicionando um número ao fim em caso de repetição.

> Atenção: Funciona apenas para os bancos **Itaú** e **Santander**!

## Funcionamento

Procura por exatamente um arquivo .pdf localizado no diretório especificado e o separa por página,
salvando cada página individualmente no diretório especificado, adicionando uma pasta para o mês
atual e outra para cada cliente, e nomeia-as de acordo com o pagador identificado.

## Instruções

> Requer o Java 8 ou superior instalado!

Copie o **jar** para uma pasta que contenha apenas **um único arquivo pdf** e execute-o passando o
diretório atual e o de destino **sem a barra final (/)** como argumentos. Os boletos serão salvos no
diretório de destino especificado, em subpastas auxiliares criadas.

Há um [bat](src/main/resources/split_pdf_boleto_nfe.bat) e um [jar](out/artifacts/) prontos por
conveniência.

### Conveniência

Se o argumento de destino não for passado, o programa funcionará e extrairá todos os .pdf na mesma
pasta de origem, sem separar por mês ou nome do cliente.
