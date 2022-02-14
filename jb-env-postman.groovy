import groovy.json.JsonOutput
import groovy.json.JsonSlurper

def iff = new File(args[0])

def jbEnv = new JsonSlurper().parse(iff) as Map<String, String>
def ks = jbEnv.keySet()

ks.each { k ->
    if (k.toString().toLowerCase() === "class") {
        return
    }
    def env = [:]
    env["name"] = k
    env["values"] = []
    env["_postman_variable_scope"] = "environment"
    def v = jbEnv[k] as Map<String, String>
    v.keySet().each {
        valit -> {
            println valit
            def val = [:]
            val["key"] = valit
            val["value"] = v[valit]
            val["enabled"] = true
            env["values"].add(val)
        }
    }
    def off = new File("./${k}.json")
    off.delete()
    off.createNewFile()
    off << JsonOutput.toJson(env)
}
