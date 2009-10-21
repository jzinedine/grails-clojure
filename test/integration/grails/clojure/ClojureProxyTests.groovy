package grails.clojure

class ClojureProxyTests extends GroovyTestCase {
    static transactional = false
    
    void testASimpleClojureFunction() {
        assertEquals 'A Simple Clojure Function', new ClojureProxy().simple()
    }
    
    void testClojureFunctionWithASingleArgument() {
        def results = new ClojureProxy().fibo(5)
        assertNotNull 'results were null', results
        assertEquals 'wrong number of elements in result', 5, results.size()
    }
    
    void testClojureFunctionWithMultipleArguments() {
        def proxy = new ClojureProxy()
        
        assertEquals 10, proxy.add_numbers(3, 2, 5)
        assertEquals 10, proxy.add_numbers(6, 4)
        assertEquals 10, proxy.add_numbers(1, 2, 3, 4)
    }

    void testClojureFunctionWithClosureArgument() {
        def proxy = new ClojureProxy()

        def testClosure = {x, y -> x + y}
        def args = [7, 3]
        assertEquals testClosure(*args), proxy.execute_closure(testClosure, *args)

        shouldFail { proxy.execute_closure({ it } *args) }
    }

    void testConvertArgs() {
        def proxy = new ClojureProxy()

        def args = [1, 2, 3]
        assertEquals args, proxy.convertArgs(args)
    }

    void testClojureFunctionWithMapArgument() {
        def proxy = new ClojureProxy()

        def testMap = [third: 'groovy', second: 'grails', first: 'clojure']
        assertEquals 'clojure', proxy.read_map(testMap, 'first')
    }

    void testClojureBinding() {
      	def proxy = new ClojureProxy()

      	assertEquals 15, proxy.fifteen
      	assertEquals "test string", proxy.test_string
      	shouldFail { proxy.blah }
    }

    void testNamespaces() {
        def proxy = new ClojureProxy()
        
        // go back and forth several times to make sure
        // the wrong function doesn't get cached and reused
        // in the wrong place
        assertEquals "one::doit", proxy['one'].doit()
        assertEquals "two::doit", proxy['two'].doit()
        assertEquals "one::doit", proxy['one'].doit()
        assertEquals "two::doit", proxy['two'].doit()
        assertEquals "one::doit", proxy['one'].doit()
        assertEquals "two::doit", proxy['two'].doit()
    }
}
