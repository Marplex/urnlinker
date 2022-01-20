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

import model.Nirlink
import java.text.SimpleDateFormat
import java.util.*

class Urnlinker {

    //Regex used to extract "decreti legge", "decreti legislativi" and "leggi"
    private val regexLeggi =
        "(decreto-legge|legge[^.]|decreto legislativo)((?:[^;]+?[0-9]{1,4}){1,3})(?:[^;]+?[ ;]){0,2}".toRegex()

    //Regex used to extract "articoli della Costituzione"
    private val regexCostituzione = "articol[oi] [^;\\n]+?della Costituzione".toRegex()

    //Regex to extract all the articles from the text (like 6 and 7 from "articoli 6 e 7 della Costituzione")
    private val selectArticoli = "[0-9]{1,3}".toRegex()

    //Regexto extract all the info used to generate a link (date and number)
    private val selectNorma = "([0-9]{1,2})[°]? +(\\w+) +([0-9]{4}).+n\\. +([0-9]{1,3})".toRegex()

    //Template used to create the URN:NIR link
    private val urnTemplate = "urn:nir:%s:%s:%s;%d"

    private val dateFormat = SimpleDateFormat("MMMM", Locale.ITALY)

    /**
     * Extract urn:nir links from the norm
     * @param string Input text of the norm (not HTML, just plain text)
     * @return List of urn links
     */
    fun link(string: String): List<Nirlink> {
        //Output list of urn:nir links
        val urnList = arrayListOf<Nirlink>()

        //Sanitize input string
        val normalized = string.replace("  ", " ")

        //Find matches for "decreti legge", "decreti legislativi" and "leggi"
        regexLeggi.findAll(normalized).forEach { match ->
            val typeNorma = match.groupValues[1].trim()
            val norma = match.groupValues[2].trim()

            //Parse text and add links to the ouput list
            generateUrnFromNormaText(typeNorma, norma)?.also {
                urnList.add(Nirlink(it, match.range))
            }
        }

        //Find matches for "articoli della Costituzione"
        regexCostituzione.findAll(normalized).forEach { match ->
            generateUrnsFromCostituzioneText(match.value)
                .map { Nirlink(it, match.range) }
                .forEach { urnList.add(it) }
        }

        return urnList
    }

    /**
     * Generate a list of urn:nir links from a text containing
     * "articolo 7 della Costituzione" or "articoli 7 e 8 della Costituzione"
     *
     * @param text Text containing the article
     * @return List of urn:nir links (could be more than one if the text contains more articles)
     */
    private fun generateUrnsFromCostituzioneText(text: String): List<String> {
        return selectArticoli.findAll(text)
            .map { it.value.toInt() }
            .map { articolo ->
                urnTemplate.format(
                    "stato",
                    "costituzione",
                    "1947-12-27",
                    articolo
                )
            }.filter { it.isNotEmpty() }
            .toList()
    }

    /**
     * Generate one urn:nir link (or null if none) from a text containing
     * "decreto-legge 25 giugno 2008, n. 112"
     *
     * @param text Text containing the norm
     * @return The urn:nir link of the text or null if none
     */
    private fun generateUrnFromNormaText(type: String, text: String): String? {
        val result = selectNorma.find(text)?.groupValues
        if (result != null) {

            //Extract info from the regex result
            val day = result[1]
            val monthString = result[2]
            val year = result[3]
            val number = result[4].toInt()

            //Parse date
            val calendar = Calendar.getInstance()
            calendar.time = dateFormat.parse(monthString)
            val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')

            return urnTemplate.format(
                "stato",
                type.replace("-", "."),
                "$year-$month-$day",
                number
            )
        } else return null
    }
}