package actors

import java.io.{File, FileWriter}
import java.nio.file.Path

import actors.msg.{WriteResult, WriteMsg}
import akka.actor.{ActorLogging, Actor}

/**
 * Serial multi­writer support through a single writer actor.
 *
 * Like was "predicted" in the Spec:
 *
 * "
 * In order to write to this file (append entries), there exists a single Actor that will
 * process all write messages serially and return an acknowledgement once an entry has been appended.
 * "
 *
 * Here it is, this actor.
 */

class WriterActor(fileName: String) extends Actor with ActorLogging {

  var lines: Long = 0
  var chars: Long = 0

  val fileWriter = new FileWriter(new File(fileName), true)

  override def preStart(): Unit = {

    log.info("WriterActor is about to start with '" + fileName + "' filename")

  }

  override def receive: Receive = {

    case msg:WriteMsg  => {
      log.info("got '" + msg.line + "' in WriterActor")


      val line = msg.line + "\n"

      fileWriter.write(line)                 // append to file
      fileWriter.flush()

      // increment the states
      lines += 1
      chars += line.length

      sender() ! WriteResult(lines, chars) // return an acknowledgement once an entry has been appended

    }

    case _ => {
      log.error("got unexpected msg in WriteActor!")
    }

  }

}
