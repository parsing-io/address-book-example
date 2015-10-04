package addressbook

import addressbook.fileprocessor.InMemoryFileProcessor
import addressbook.lineprocessor.{LowLevelOptimizedLineProcessor, RegexLineProcessor}
import org.scalatest.Spec

/**
 *
 */
//noinspection UnitMethodIsParameterless
class SpecTest extends Spec {

    val linePattern = Util.conf.getString("exercise.parsing.linePattern")
    val gumtreeLines = "Bill McKnight, Male, 16/03/77" :: "Paul Robinson, Male, 15/01/85" :: "Gemma Lane, Female, 20/11/91" :: "Sarah Stone, Female, 20/09/80" :: "Wes Jackson, Male, 14/08/74" :: Nil
    val oneHundredLines = "KNLUW QTRCKLJNHQ, Female, 12/10/71" :: "ZGQG BUOUGEJ, Male, 14/02/84" :: "UZPSHM TICK, Male, 18/05/83" :: "PYWAHUJTA XEOQNBJ, Male, 19/01/03" :: "HQDH ZVILB, Male, 12/02/96" :: "SKXW PUHCUKJELCI, Male, 27/09/84" :: "JXOPTIBD CTJVPIOY, Female, 11/04/84" :: "OOHCFHLU IUJB, Male, 20/04/05" :: "XRKZIIMFX BSZJG, Male, 24/11/80" :: "MJPFY ORWTDWVH, Female, 18/10/70" :: "OKVWYERA OXLGZEUEW, Female, 29/02/93" :: "IJSCG CJJNQYMKIT, Female, 17/03/88" :: "XNLXDA ISJTYFV, Male, 03/03/78" :: "Bill McKnight, Male, 16/03/77" :: "Paul Robinson, Male, 15/01/85" :: "YPKH NSKAFHW, Male, 17/09/71" :: "EEUDEVW TMFPWMGB, Male, 16/06/90" :: "TAQILP UXGI, Male, 05/06/84" :: "BANAWCPPA YFADNZD, Male, 13/07/00" :: "HOAVHYQIZ TIQJ, Male, 04/06/86" :: "JKUMG MNCZALFMMT, Male, 28/04/80" :: "CYWLP CVDLZNLMQI, Female, 21/04/83" :: "JEHN RDWBIWC, Male, 24/05/03" :: "OSRUAJH OXKFRE, Male, 26/12/75" :: "PMFXLIQ KIHYILQN, Male, 27/01/86" :: "KCTCSUID XBFSHUZDNM, Male, 13/09/05" :: "WTEVIYXM IMPANHM, Female, 31/11/01" :: "LZBGKBSV LVXYHPVL, Male, 13/01/89" :: "JQOPUHN MAHCTAW, Male, 10/04/86" :: "UNTETFP KRVY, Male, 25/01/85" :: "NEYGSEMGQ YFQLEH, Female, 25/09/90" :: "GFLBCQ XTEC, Male, 10/10/73" :: "DEAZVGMPF UJSUVUG, Female, 19/10/72" :: "AQWA MZZK, Female, 30/02/97" :: "UVTCPQ BEEL, Female, 12/06/73" :: "WBTUQ DFXIHZ, Male, 05/10/97" :: "RQOTICG HWUBNPJNRXB, Male, 22/09/07" :: "OVAICOZDL VCOWF, Female, 12/01/87" :: "Gemma Lane, Female, 20/11/91" :: "AFGDRI HEQKLEW, Male, 13/06/84" :: "BOTYAYWVK XWSKJ, Male, 11/06/73" :: "JMHF LOQMWVVZM, Male, 08/12/04" :: "DORNMKBLD HDYOWXIDAG, Male, 26/07/05" :: "XRKW JNDUIN, Female, 10/06/75" :: "SCLITQJSC HYKAPUMZ, Female, 04/01/00" :: "UJZA MPXVH, Female, 30/03/72" :: "XCLED BMYCHP, Male, 31/10/84" :: "EXMUXDARW KBDME, Male, 04/01/09" :: "OFVVXKKQS CCBZMICK, Female, 01/10/97" :: "LUYEPU GOWB, Female, 20/08/76" :: "YGVKT YWCOTIYCL, Male, 03/11/94" :: "HJRNDH YRAMWNZFOGY, Female, 17/11/91" :: "GRAIYWLV RXWNPIIW, Male, 12/03/06" :: "RZOLJHMNA KOCIKSPW, Female, 10/12/79" :: "CGIGZCU EQHDEVHJGPA, Female, 10/11/00" :: "EPIWZQ UYJPUFU, Female, 19/12/01" :: "Sarah Stone, Female, 20/09/80" :: "DPULS QOTAU, Male, 01/02/87" :: "XGKKFI FFNXR, Male, 19/02/81" :: "NSHQOBH PQXZTVIAP, Female, 17/02/96" :: "BFKHCOE BMCRQHIP, Female, 13/03/78" :: "CIMH TJQSZSCS, Female, 28/08/77" :: "KJGBNUV IRLJUFPQFT, Female, 16/12/88" :: "IQZYL TQZM, Female, 24/09/88" :: "CMKOETPNN PRNC, Male, 24/07/01" :: "TIELG YSRFRZGSJ, Female, 27/10/07" :: "TWWTMQP OWBDKTCF, Female, 13/04/76" :: "RMDHE ROJEH, Male, 19/12/99" :: "GRLOZKRYC BEWAXI, Male, 18/08/94" :: "CGPDDYIX UXOLLEI, Male, 22/09/78" :: "LRETTJA QDPLMX, Female, 16/04/79" :: "FHMTQJER CAMODJVMJDB, Male, 25/01/78" :: "ZBOZCLK DQLQRL, Male, 19/09/72" :: "QALCALK HMPAQE, Female, 30/10/04" :: "Wes Jackson, Male, 14/08/74" :: "FHAYFX OZTETRJB, Male, 09/08/98" :: "RJJGS WYYCXG, Female, 08/09/00" :: "UFSNP WRBQOQ, Female, 25/11/83" :: "DRDJP OJGWIQQXX, Male, 21/07/79" :: "MTFXMNYJ VXWOCEZWH, Male, 25/04/75" :: "HTRFNUG KSCAXOD, Female, 23/05/78" :: "RIQNKOBX LPVJHDN, Male, 01/12/89" :: "NHZSKFR ZHFWJEGZ, Male, 04/11/04" :: "Jeralean Talley, Male, 03/06/70" :: "Emma Morano-Martinuzzi, Female, 02/06/70" :: "DZESFPOD WVFNYRM, Male, 19/07/80" :: "NPAGESEP OOBH, Male, 26/06/97" :: "XSRF PWQHQWSTY, Female, 21/09/78" :: "HLOCCRDZI TDVWRUPBCW, Female, 06/05/06" :: "IHHJJFR GBJXXWHJ, Female, 24/07/05" :: "GQMVMAAX LSXGEGQBIAP, Male, 16/05/89" :: "TQTTBM UFWCLSVOAL, Female, 06/03/78" :: "XSETDYG IETXHGWWGP, Female, 01/04/87" :: "LNALHMB IFFZUGI, Female, 16/02/86" :: "IWEDDS VEFYTPQY, Female, 19/05/78" :: "ZOJD XDGSGHNKE, Male, 11/07/02" :: "CFDVPUBCN UULLTFGSR, Female, 28/02/92" :: "GGBAW JHKHM, Female, 10/05/98" :: "CPFLI FWMA, Male, 21/03/86" :: "TEBKD JKPBLEUHL, Male, 08/07/94" :: Nil

    object `address book processor` {

        object `regex line processor` {

            object `valid address book` {

                object `gumtree lines` {

                    val fileProcessor = InMemoryFileProcessor(RegexLineProcessor(validate = true, linePattern, ", ", ",\\s", "dd/MM/yy", "/"), "dummy-pathname", "UTF-8", gumtreeLines)
                    fileProcessor.processFile()

                    def `should find 3 males` = assert(fileProcessor.lineProcessor.malesCount == 3)

                    def `should find Wes the oldest` = assert(fileProcessor.lineProcessor.oldest.get.name == "Wes Jackson")

                    def `should compute how many days is Bill older than Paul` = assert(fileProcessor.lineProcessor.olderByDays.get == 2862)
                }

                object `one hundred lines` {
                    val fileProcessor = InMemoryFileProcessor(RegexLineProcessor(validate = true, linePattern, ", ", ",\\s", "dd/MM/yy", "/"), "dummy-pathname", "UTF-8", oneHundredLines)
                    fileProcessor.processFile()

                    def `should find 55 males` = assert(fileProcessor.lineProcessor.malesCount == 55)

                    def `should find Emma the oldest` = assert(fileProcessor.lineProcessor.oldest.get.name == "Emma Morano-Martinuzzi")

                    def `should compute how many days is Bill older than Paul` = assert(fileProcessor.lineProcessor.olderByDays.get == 2862)
                }

            }

            object `invalid address book` {

                object `malformed gumtree lines` {

                    object `empty address book` {
                        val fileProcessor = InMemoryFileProcessor(RegexLineProcessor(validate = true, linePattern, ", ", ",\\s", "dd/MM/yy", "/"), "dummy-pathname", "UTF-8", List.empty[String])
                        fileProcessor.processFile()

                        def `should find no males` = assert(fileProcessor.lineProcessor.malesCount == 0)

                        def `should not find the oldest` = assert(fileProcessor.lineProcessor.oldest.isEmpty)

                        def `should not compute how many days is Bill older than Paul` = assert(fileProcessor.lineProcessor.olderByDays.isEmpty)
                    }

                    object `line-fraction address book` {
                        val fileProcessor = InMemoryFileProcessor(RegexLineProcessor(validate = true, linePattern, ", ", ",\\s", "dd/MM/yy", "/"), "dummy-pathname", "UTF-8", List("\\t"))
                        fileProcessor.processFile()

                        def `should find no males` = assert(fileProcessor.lineProcessor.malesCount == 0)

                        def `should not find the oldest` = assert(fileProcessor.lineProcessor.oldest.isEmpty)

                        def `should not compute how many days is Bill older than Paul` = assert(fileProcessor.lineProcessor.olderByDays.isEmpty)
                    }

                    object `malformed individual lines` {
                        val lineProcessor: RegexLineProcessor = RegexLineProcessor(validate = true, linePattern, ", ", ",\\s", "dd/MM/yy", "/")

                        // invalid individual lines
                        def `should detect invalid delimiter` = assert(!lineProcessor.processLine("Bill McKnight,  Male, 16/03/77"))

                        def `should detect one token extra` = assert(!lineProcessor.processLine("Bill McKnight, , Male, 16/03/77"))

                        def `should detect invalid line pattern` = assert(!lineProcessor.processLine("Bill McKnight, 16/03/77, Male"))

                        def `should detect invalid sex` = assert(!lineProcessor.processLine("Gemma Lane, Femme, 20/11/91"))

                        def `should detect invalid day` = assert(!lineProcessor.processLine("Bill McKnight, Male, 32/03/77"))

                        def `should detect invalid month` = assert(!lineProcessor.processLine("Bill McKnight, Male, 16/3/77"))

                        def `should detect invalid date pattern` = assert(!lineProcessor.processLine("Bill McKnight, Male, 03/16/77"))
                    }

                }

            }

        }

        // low-level optimized line processor does not support validation; hence only valid data are passed to
        object `low level optimized line processor` {

            object `gumtree lines` {

                val fileProcessor = InMemoryFileProcessor(LowLevelOptimizedLineProcessor(", ", ",\\s", "dd/MM/yy", "/"), "dummy-pathname", "UTF-8", gumtreeLines)
                fileProcessor.processFile()

                def `should find 3 males` = assert(fileProcessor.lineProcessor.malesCount == 3)

                def `should find Wes the oldest` = assert(fileProcessor.lineProcessor.oldest.get.name == "Wes Jackson")

                def `should compute how many days is Bill older than Paul` = assert(fileProcessor.lineProcessor.olderByDays.get == 2862)
            }

            object `one hundred lines` {
                val fileProcessor = InMemoryFileProcessor(LowLevelOptimizedLineProcessor(", ", ",\\s", "dd/MM/yy", "/"), "dummy-pathname", "UTF-8", oneHundredLines)
                fileProcessor.processFile()

                def `should find 55 males` = assert(fileProcessor.lineProcessor.malesCount == 55)

                def `should find Emma the oldest` = assert(fileProcessor.lineProcessor.oldest.get.name == "Emma Morano-Martinuzzi")

                def `should compute how many days is Bill older than Paul` = assert(fileProcessor.lineProcessor.olderByDays.get == 2862)
            }

        }

    }

}

