# photoManagement

System for own photomanagement with spring and phonegap

## Resumen del Proyecto

Este es un proyecto de backend para una aplicación de gestión de fotos. El propósito principal es ofrecer una API REST para que una aplicación cliente (posiblemente una aplicación móvil creada con PhoneGap) pueda sincronizar y gestionar fotos.

## Tecnologías Utilizadas

*   **Java:** El lenguaje de programación principal.
*   **Spring Boot:** Proporciona la base para crear una aplicación web autónoma y autocontenida.
*   **Spring Web:** Se utiliza para construir la API REST (endpoints HTTP).
*   **Spring Data JPA:** Facilita la interacción con la base de datos a través de repositorios.
*   **PostgreSQL:** La base de datos relacional utilizada para almacenar metadatos e información de las fotos.
*   **im4java:** Una librería para el procesamiento de imágenes, probablemente utilizada para crear miniaturas o modificar imágenes.
*   **metadata-extractor:** Una librería para leer metadatos de los archivos de imagen (ej. fecha, ubicación, datos EXIF).
*   **Maven:** El gestor de dependencias y construcción del proyecto.

## Funcionalidad Implementada (Inferida)

Basado en el análisis de las dependencias, se puede inferir la siguiente funcionalidad:

1.  **API REST:** Expone un conjunto de endpoints HTTP para que un cliente pueda interactuar con el sistema.
2.  **Carga de Archivos:** Permite a los usuarios subir archivos de fotos al servidor.
3.  **Extracción de Metadatos:** El sistema puede extraer y guardar metadatos importantes de las fotos (fecha, modelo de cámara, ubicación GPS, etc.) en la base de datos.
4.  **Procesamiento de Imágenes:** Es muy probable que el sistema genere versiones más pequeñas de las imágenes (miniaturas) para optimizar la carga en las galerías de la aplicación cliente.
5.  **Persistencia de Datos:** Almacena la información y metadatos de las fotos en una base de datos PostgreSQL, lo que permite realizar búsquedas, organización y gestión de las mismas.
