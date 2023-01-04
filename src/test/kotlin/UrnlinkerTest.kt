/*
 * Copyright (c) 2022 Marco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import com.github.marplex.Urnlinker
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UrnlinkerTest {

    private val urnLinker = Urnlinker()

    @Test
    fun shouldLinkArticoliCostituzione() {

        //Single article
        var string =
            "Visto l'articolo 16 della Costituzione,  che  consente  limitazioni della liberta' di circolazione per ragioni sanitarie"
        var links = urnLinker.link(string)

        assertEquals(1, links.size)
        assertEquals("urn:nir:stato:costituzione:1947-12-27;16", links.first().urn)

        //Multiple articles
        string = "Visti  gli  articoli  32  e  117,  secondo  e  terzo  comma,  della Costituzione;"
        links = urnLinker.link(string)

        assertEquals(2, links.size)
        assertEquals("urn:nir:stato:costituzione:1947-12-27;32", links[0].urn)
        assertEquals("urn:nir:stato:costituzione:1947-12-27;117", links[1].urn)
    }

    @Test
    fun shouldLinkDecretiLegge() {
        //Single article
        val string = "Visto il decreto-legge 16  maggio  2020,  n.  33,  convertito,  con modificazioni"
        val links = urnLinker.link(string)

        assertEquals(1, links.size)
        assertEquals("urn:nir:stato:decreto.legge:2020-05-16;33", links.first().urn)
    }

    @Test
    fun shouldLinkLeggi() {
        //Single article
        val string = """
            convertito,  con
            modificazioni, dalla legge 28 maggio 2021,  n.  76,  recante  «Misure
            urgenti per il contenimento dell'epidemia da COVID-19, in materia  di
            vaccinazioni anti SARS-CoV-2, di giustizia e di concorsi pubblici»;
        """.trimIndent()

        val links = urnLinker.link(string)

        assertEquals(1, links.size)
        assertEquals("urn:nir:stato:legge:2021-05-28;76", links.first().urn)
    }

    @Test
    fun shouldLinkShortformLeggi() {
        //Short form single article
        val string = "((PROVVEDIMENTO ABROGATO DALLA L. 29 GENNAIO 2021, N. 7))"

        val links = urnLinker.link(string)

        assertEquals(1, links.size)
        assertEquals("urn:nir:stato:legge:2021-01-29;7", links.first().urn)
    }

}