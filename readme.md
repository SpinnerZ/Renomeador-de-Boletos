# Separador e Renomeador de PDFs para Contabilidade

## Objetivo

Aplicação para auxiliar no processo de dividir e renomear pdfs de boletos e notas fiscais.

Ao gerar boletos de cobrança para clientes, é comum que o banco gere **um único arquivo PDF com
todos os boletos de todos os clientes**. O mesmo vale para a emissão de **Notas Fiscais
Eletrônicas** da prefeitura.

Esse programa lê uma série arquivos pdf contendo vários boletos e NFe de vários clientes e os
separa, salvando cada arquivo pdf na pasta indicada com o nome do cliente correspondente,
adicionando um número ao fim em caso de repetição.

> Atenção: Funciona apenas para os bancos **Itaú** e **Santander**, e NFe da **Prefeitura do Recife**!

## Funcionamento

Procura por uma lista de arquivos .pdf localizado no diretório de origem especificado e o separa por
página, salvando cada página individualmente no diretório de destino especificado, ou cria uma pasta
se apenas a origem foi fornecida, e nomeia-as segundo o cliente identificado.

## Instruções

> Requer o Java 8 ou superior instalado!

Copie o **jar** para uma pasta que contenha os arquivos pdf e execute-o ou passe o diretório de
origem e o de destino **sem a barra final (/)** como argumentos. O programa aceita ainda apenas o
diretório de origem. Os boletos serão salvos no diretório de destino especificado, ou criará uma
pasta chamada "Processados" dentro do diretório de origem.

Há um [bat](src/main/resources/split_pdf_boleto_nfe.bat) e um [jar](out/artifacts/) prontos por
conveniência.
