package actors

import actors.msg.ReadMsg
import akka.actor.{ActorLogging, Actor}

/**
 *
 * "
 * History provides concurrent reads by creating multiple actors that
 * can read from independent locations in the file.
 * "
 *
 * "
 * These actors hold onto their reading position in the file so they know where to read from next
 * time they are asked to read another entry"
 *
 *
 * "
 * Readers can return an “EOF” message if they encounter an end of file;
 * and the user can decide to retry the read after some time.
 * "
 *
 */
class ReaderActor(fileName: String) extends Actor with ActorLogging {

  var currentPos = 0 // reading position in the file (mutable state)

  override def preStart(): Unit = {

    log.info("ReaderActor is about to start with '" + fileName + "' filename")

  }

  override def receive: Receive = {

    case msg: ReadMsg => {

      // TODO:
      // case: read next
      // case: read random

      log.info("got ReadMsg for line number: " + msg.position)
    }

    case _ => {
      log.error("got unexpected msg in ReaderActor")
    }

  }

}
