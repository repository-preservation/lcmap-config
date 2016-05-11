# LCMAP Configuration

#### Contents

* [About](#about-)
* [Usage](#usage-)
* [License](#usage-)

## About [&#x219F;](#contents)

This project provides helpers to parse, coerce, and validate values from a variety of sources (INI, EDN, CLI, ENV). It also supplies configuration component in order to make it easy to supply other components with configuration values..

## Usage [&#x219F;](#contents)

In order to use lcmap-config you need to:

1. Add a dependency to project.clj
2. Define a schema to coerce and validate values
3. Add keys/values to lcmap.ini
4. Update your components

### Dependency

```
[lcmap-config 0.5.0-SNAPSHOT]
```

### Define a Schema

For example:

```
(ns lcmap.project.config)

(def cfg-schema
  {schema/Keyword schema/Any})

(def defaults
  {:ini *lcmap-config-ini*
   :args *command-line-args*
   :schema cfg-schema})
```

### Update lcmap.ini

By default LCMAP projects read configuration from $HOME/.usgs/lcmap.ini

The convention is to create a section based on the project's namespace. For example, the lcmap-data project expects a section called lcmap.data

[lcmap.data]
db-hosts=host1, host2, host3

## Update components

LCMAP projects expect the configuration component to be associated with a key of `:cfg` in the system map.

```
(component/system-map
#_...
  :cfg (component/using
         (config/new-configuration opts)
          []))
#_...
```


Use the first and second level namespace ... wording ... e.g. lcmap.data, lcmap.rest, lcmap.client, etc...

# License [&#x219F;](#contents)

Copyright © 2015-2016 United States Government

NASA Open Source Agreement, Version 1.3
