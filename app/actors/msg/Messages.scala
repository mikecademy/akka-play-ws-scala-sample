package actors.msg

// commands
trait Command

case class ReadMsg(position:Long) extends Command

case class WriteMsg(line:String) extends Command


// events
trait Event

object EOF extends Event