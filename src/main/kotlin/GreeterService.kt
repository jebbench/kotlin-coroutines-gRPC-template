import io.grpc.examples.helloworld.GreeterCoroutineGrpc
import io.grpc.examples.helloworld.HelloReply
import io.grpc.examples.helloworld.HelloRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

class GreeterService : GreeterCoroutineGrpc.GreeterImplBase() {

    val myThreadLocal = ThreadLocal.withInitial { "value" }.asContextElement()

    override val initialContext: CoroutineContext
        get() = Dispatchers.Default + myThreadLocal

    override suspend fun sayHelloClientStreaming(
        requestChannel: ReceiveChannel<HelloRequest>
    ): HelloReply {

        for(req in requestChannel){
            println("Received name: ${req.name}")
        }

        return HelloReply.newBuilder()
            .setMessage("Completed without receiving an Exception")
            .build()
    }
}