# touchme

A Leiningen plugin to do many wonderful things.

## Usage

Put `[touchme "0.1.0"]` into the `:plugins` vector of your `project.clj`.

    $ lein touchme

## Configuration

Add a `:touchme-config` map into the `project.clj`:

- `:path-to-observe` -> relative to project path of files to observe
- `:extension-to-observe` -> extension of files to observe
- `:file-to-touch` -> path of the file to touch. Relative to the first
`source-paths`
- `:path-of-files-to-touch` -> base path of files to touch. Relative to the
first `source-paths`. File to touch will be
`source-paths/path-of-files-to-touch/path-of-updated-file.clj`

## Examples

### Configuration when only one file can be touched.

```clojure
(defproject touchme-single-example "0.0.1"
  ;description, url, ...
  :plugins [[touchme "0.1.0"]]
  :touchme-config {:path-to-observe "resources/templates"
                   :extension-to-observe ".html"
                   :file-to-touch "my-project/templates/template.clj"})
```

When `./resources/templates/**/*.html` is modified, the file
`./src/my-project/templates/template.clj` is touched.

### Configuration when same file name can be touched.

```clojure
(defproject touchme-single-example "0.0.1"
  ;description, url, ...
  :plugins [[touchme "0.1.0"]]
  :touchme-config {:path-to-observe "resources/templates"
                   :extension-to-observe ".html"
                   :path-of-files-to-touch "my-project/templates/"})
```

When `./resources/templates/**/*.html` is modified, the file
`./src/my-project/templates/<filepath>.clj` is touched. By example if
`./resources/templates/input/entry.html` is modified,
`./src/my-project/templates/input/entry.clj` is touched.

### Others

See [lein-touchme-example](https://github.com/sogilis/lein-touchme-example) for
an example to run.

## License

Copyright © 2013 Sogilis

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
