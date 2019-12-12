import com.github.marcoferrer.krotoplus.coroutines.launchProducerJob
import com.github.marcoferrer.krotoplus.coroutines.withCoroutineContext
import io.grpc.Channel
import io.grpc.Server
import io.grpc.examples.helloworld.GreeterCoroutineGrpc
import io.grpc.examples.helloworld.send
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import kotlinx.coroutines.*

val server: Server = InProcessServerBuilder
    .forName("helloworld")
    .addService(GreeterService())
    .build()
    .start()

val channel: Channel = InProcessChannelBuilder
    .forName("helloworld")
    .build()

suspend fun main(){

    // Optional coroutineContext. Default is Dispatchers.Unconfined
    val stub = GreeterCoroutineGrpc
        .newStub(channel)
        .withCoroutineContext()

    performClientStreamingCall(stub)

    server.shutdown()
}


suspend fun performClientStreamingCall(stub: GreeterCoroutineGrpc.GreeterCoroutineStub) = coroutineScope{

    // Client Streaming RPC
    val (requestChannel, response) = stub.sayHelloClientStreaming()

    requestChannel.send { name = "First" }
    requestChannel.close(Exception("This is an exception!"))

    println("Client Streaming Response: ${response.await().toString().trim()}")
}