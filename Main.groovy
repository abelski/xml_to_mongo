import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.MongoClient
@Grab(group = 'org.mongodb', module = 'mongo-java-driver', version = '3.8.2')
@Grab(group = 'org.json', module = 'json', version = '20140107')

import groovy.util.slurpersupport.NodeChild

def mongoClient = new MongoClient();
DBCollection collection = mongoClient.getDB("agile").getCollection("tickets")
collection.drop()
println "Start"
def xml = new XmlSlurper().parse(new File(args[0]))
def items = xml.depthFirst().findAll { it.name() == 'item' }
items.each { it ->
    println it.title
    put(it, collection) }
println "finded $items.size() items"

private put(NodeChild node, DBCollection collection) {
    BasicDBObject basicDBObject = new BasicDBObject()
    node.childNodes().each { it ->
        def writer = new StringWriter()
        it.writeTo(writer)
        basicDBObject.put(it.name, writer.toString())
    }
    collection.insert(basicDBObject)
}


