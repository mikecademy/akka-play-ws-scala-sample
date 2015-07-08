package actors

import java.io.RandomAccessFile

import actors.msg.{ReadNextMsg, ReadAtMsg}
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

  var position = 0 // where start reading from (mutable state)

  val file = new RandomAccessFile(fileName, "r")

  override def preStart(): Unit = {

    log.info("ReaderActor is about to start with '" + fileName + "' filename")

  }

  override def receive: Receive = {

    case ReadNextMsg => {

      log.info("got ReadNextMsg")

      file.seek(position) // go to the current position in the file

      val line = file.readLine()

      log.info("has read next line '" + line + "' at position " + position)

      position += line.length     // increment position
    }

    case msg: ReadAtMsg => {

      log.info("got ReadAtMsg for line number: " + msg.position)

      file.seek(msg.position) // go to the position in the file

      val line = file.readLine()

      log.info("the line at " + msg.position + " is '" + line + "'")
    }

    case _ => {
      log.error("got unexpected msg in ReaderActor")
    }

  }

}
