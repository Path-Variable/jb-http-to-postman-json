import groovy.json.JsonOutput

def iff = new File(args[0])
def off = new File(args[1])
def items = []
def item = [:]
def req = [:]
def headers = []
def body = [:]
def url = [:]
def query = []
def data = ""
item.put("request", req)
req.put("body", body)
req.put("header", headers) // not a typo
req.put("url", url)
url.put("query", query)
int count = 0
def startedJson = false
def today = new Date().format("YYYYMMddhhmmss")
def info = ["name": "jb-export-$today",
            "schema" : "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"]
iff.eachLine {
    def l = it.strip()
    switch (l) {
        case l.startsWith("# "): break
        case {l ==~ "(GET|PUT|POST|DELETE|OPTIONS).+"}:
            item.put("name", "request-${++count}")
            req.put("method", l[0..3])
            def rawUrl = l.split(" ")[1]
            url.put("raw", rawUrl)
            if (rawUrl.contains("://")) {
                def extUrl = rawUrl.split("://")
                url.put("protocol", extUrl[0])
                url.put("host", extUrl[1].split("/")?[0]?.split("\\."))
            } else {
                url.put("host",rawUrl.split("/")?[0]?.split("\\."))

            }
            // assumption - there should be no url encoding going here since jb does it for you
            if (!rawUrl.contains("?")) break
            rawUrl.split("\\?")?[1].split("&")?.each {
                def spq = it.split("=")
                query.add(["key": spq[0].trim(), "value" : spq[1].trim()])
            }
            break
        case {l.contains(":") && !startedJson}:
            def sph = l.split(":")
            def header = ["key":sph[0].trim(), "value" : sph[1].trim(), "type" : "text"]
            headers.add(header)
            break

        case {l.startsWith("{")}:
            if (l.endsWith("}")) {
                req.put("data",l)
                startedJson = false
            }
            body.put("mode", "raw")
            body.put("options", ["raw": ["language": "json"]])
            startedJson = true
            data += "$l\n"
            break

        case {l.contains(":") && startedJson}:
            data += l
            break

        case {l.endsWith("}") && startedJson}:
            data += l
            if (data) {
                body.put("raw", data)
            }
            startedJson = false
            break
        case {l.startsWith("###")}:
            items.add(item)
            item = [:]
            l.strip()
            req = [:]
            headers = []
            body = [:]
            url = [:]
            query = []
            data = ""
            item.put("request", req)
            req.put("body", body)
            req.put("header", headers) // not a typo
            req.put("url", url)
            url.put("query", query)
    }
}

def collection = ["info": info, "items" : items]
off.delete()
off.createNewFile()
off << JsonOutput.toJson(collection)
