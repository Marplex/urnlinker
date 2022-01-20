<h1 align="center">📜 Urnlinker</h1>
<p align="center">
 A kotlin library used to extract hyperlinks pointing to normative references<br>
</p>
<br>

<p align="center">
  <a href="https://github.com/Marplex/urnlinker/actions"><img alt="Tests passed" src="https://github.com/Marplex/urnlinker/workflows/Java%20CI/badge.svg"/></a>
  <a href="https://jitpack.io/#marplex/urnlinker"><img alt="JitPack" src="https://jitpack.io/v/Marplex/urnlinker.svg"/></a>
  <a href="https://opensource.org/licenses/MIT"><img alt="License" src="https://img.shields.io/badge/license-MIT-green"/></a>
  <a href="https://github.com/Marplex"><img alt="License" src="https://img.shields.io/static/v1?label=GitHub&message=marplex&color=005cb2"/></a> 
</p>

## Features

- [x] Extract "articolo NUMBER della Costituzione"
- [x] Extract "articoli NUMBER e NUMBER della Costituzione"
- [x] Extract "legge DAY MONTH YEAR, n. NUMBER"
- [x] Extract "decreto-legge DAY MONTH YEAR, n. NUMBER"
- [x] Extract "decreto-legislativo DAY MONTH YEAR, n. NUMBER"
- [ ] Extract "decreto del Presidente della Repubblica DAY MONTH YEAR"

## Installation

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

```gradle
implementation 'com.github.marplex:urnlinker:1.0'
```

## Usage

Extract urn:nir links from text

```kotlin
val urnLinker = Urnlinker()
val text = "Visto l'articolo 16 della Costituzione,  che  consente  limitazioni della liberta' di circolazione per ragioni sanitarie"

var links = urnLinker.link(text)

//Output
//links[0] == urn:nir:stato:costituzione:1947-12-27;16
```

## License

```xml
Copyright (c) 2022 Marco

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.```