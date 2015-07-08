package actors

import actors.msg.{WriteMsg, ReadMsg}
import akka.actor._
import akka.routing._


/**
 Both the WriterActor and the ReaderActors are controlled by a "manager actor".

 This top­level actor
  - delegates messages to the WriterActor when needed
  - spawns, maintains and forwards messages to ReaderActors.

  This top level 'manager actor' represents the entry point to the History library.

  * @param fileName - a file name to write and to read from.
*/

class HistoryActor(fileName:String) extends Actor with ActorLogging {

  // create a child actors


 val writerActor = context.actorOf(
                                    Props(classOf[WriterActor], fileName),
                                    name = "writer-actor")                    // one writer

  val workerActors = context.actorOf(
                                      Props(classOf[ReaderActor], fileName).withRouter(RoundRobinPool(4)),
                                      name = "reader-actor")

  override def receive: Receive = {

    case cmd : ReadMsg => {

      log.info("forwarding " + cmd + " to the reader" )

      //routerReaders.route(cmd, sender())

      workerActors forward  cmd

    }

    case cmd: WriteMsg => {

      log.info("forwarding " + cmd + " to the writer" )

      writerActor forward cmd

    }

    case _ => {
      log.error("error!")
    }

//    case Terminated(a) =>
//      // TODO: decide
  }

}
