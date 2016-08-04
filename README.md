# lcmap.config

[![Build Status][travis-badge]][travis][![Dependencies Status][deps-badge]][deps][![Clojars Project][clojars-badge]][clojars]

*Unified Configuration for the LCMAP System of Systems*

[![LCMAP open source project logo][lcmap-logo]][lcmap-logo-large]


#### Contents

* [About](#about-)
* [Documentation](#documentation-)
* [Usage](#usage-)
  * [project.clj Dependency](#projectclj-dependency-)
  * [Defining a Config Schema](#defining-a-config-schema-)
  * [Configuration File](#configuration-file-)
  * [Components & Configuration](#components--configuration-)
* [License](#license-)


## About [&#x219F;](#contents)

This project provides helpers to parse, coerce, and validate values from a variety of sources (INI, EDN, CLI, ENV). It also supplies configuration component in order to make it easy to supply other components with configuration values..


## Documentation [&#x219F;](#contents)

The LCMAP configuration system API reference is slowly being updated with
docstrings.  The project's auto-generated documentation is available here:

* [http://usgs-eros.github.io/lcmap-event](http://usgs-eros.github.io/lcmap-event)


## Usage [&#x219F;](#contents)

In order to use lcmap-config you need to:

1. Add a dependency to project.clj
2. Define a schema to coerce and validate values
3. Add keys/values to lcmap.ini
4. Update your components


### ``project.clj`` Dependency [&#x219F;](#contents)

```
[lcmap-config 0.5.0-SNAPSHOT]
```


### Defining a Config Schema [&#x219F;](#contents)

For example:

```clj
(ns lcmap.project.config)

(def cfg-schema
  {schema/Keyword schema/Any})

(def defaults
  {:ini *lcmap-config-ini*
   :args *command-line-args*
   :schema cfg-schema})
```


### Configuration File [&#x219F;](#contents)

By default LCMAP projects read configuration from `$HOME/.usgs/lcmap.ini`.

The convention is to create a section based on the project's namespace. For example, the lcmap-data project expects a section called `lcmap.data`:

```ini
[lcmap.data]
db-hosts=host1, host2, host3
```

### Components & Configuration [&#x219F;](#contents)

LCMAP projects expect the configuration component to be associated with a key of `:cfg` in the system map.

```clj
(component/system-map
  ...
  :cfg (component/using
         (config/new-configuration opts)
          []))
  ...)
```


Use the first and second level namespace ... wording ... e.g. `lcmap.data`, `lcmap.rest`, `lcmap.client`, etc.


# License [&#x219F;](#contents)

Copyright © 2015-2016 United States Government

NASA Open Source Agreement, Version 1.3


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/USGS-EROS/lcmap-config
[travis-badge]: https://travis-ci.org/USGS-EROS/lcmap-config.png?branch=master
[deps]: http://jarkeeper.com/usgs-eros/lcmap-config
[deps-badge]: http://jarkeeper.com/usgs-eros/lcmap-config/status.svg
[lcmap-logo]: https://raw.githubusercontent.com/USGS-EROS/lcmap-system/master/resources/images/lcmap-logo-1-250px.png
[lcmap-logo-large]: https://raw.githubusercontent.com/USGS-EROS/lcmap-system/master/resources/images/lcmap-logo-1-1000px.png
[clojars]: https://clojars.org/gov.usgs.eros/lcmap-config
[clojars-badge]: https://img.shields.io/clojars/v/gov.usgs.eros/lcmap-config.svg
[tag-badge]: https://img.shields.io/github/tag/usgs-eros/lcmap-config.svg?maxAge=2592000
[tag]: https://github.com/usgs-eros/lcmap-config/tags
