package actors.msg

// commands
trait Command

case class ReadMsg(position:Long) extends Command

case class WriteMsg(line:String) extends Command


// events
trait Event

case class WriteResult(lines:Long, chars:Long)

object EOF extends Event