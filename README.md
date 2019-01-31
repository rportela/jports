# J Ports: Aspect implementations

This project is a collection of implementations that were built with modularization of concerns in mind. It uses a lot of annotations and specially crafted classes around an **Aspect** idea. It also contains guiding implementations such as **Action** and **ActionExecution** that can be instantiated from the web or from the standard console and unify validation, exception handling, logging and state management. 

## Actions

Code to help developers encapsulate reusable code in classes that can be called from the web or from the console. We named them **Action**. Every action receives parameterized parameters and produces parameterized outputs. The execution is wrapped in an **ActionExecution** class that simplifies input/output processing, logging and debugging. Several serialization methods have been written to allow consuming Actions through HTTP transport, such as Request Parameter Parser, JSON Parser, Multipart Parser and XML Parser. 

## Adapters

The adapters package contains utility classes for converting between data types. We all know that the core Java methods and type conversions are not straight forward and we provide simplicity by converting almost every numeric, string and byte array data type to one another. These adapters are also incorporated in data, text and XML aspects to provide custom data transformation options.

## Calendar

A calendar implementation with the sole responsibility of finding holidays and business days. We added a full implementation of fixed and recurring Brazilian holidays including easter, carnival and other religious dates calculated from moon phases. We kindly invite you to add implementations for your country and do a pull request.

## Data

... TODO ... write documentation

## Database

... TODO ... write documentation

## Reflection

This package wraps the java reflect package and creates an extensible **Aspect** idea. An aspect is something that traverses specific implementations. It is based on java annotations and implementations not coupled to any particular code. You can check out the **DatabaseAspect**, **ValidationAspect** or **XmlAspect** for specific implementations of this idea. 

## Text

A full blown Create, Read, Update, Delete storage for CSV and Fixed Length files mapped to java classes using annotated classes with tables and columns.

## Validations

... TODO ... write documentation

## Xml

... TODO ... write documentation