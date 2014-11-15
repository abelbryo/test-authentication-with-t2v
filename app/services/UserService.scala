package services

import models._
import scala.slick.lifted.Parameters
import scala.slick.session.Session
import play.api.db.slick.Config.driver.simple._
import org.virtuslab.unicorn.ids.services._


trait UserQueries extends BaseIdQueries[UserId, User]{
  override def table = Users

  protected val findByEmail = for{
    email <- Parameters[String]
    user <- Users if user.email === email
  } yield user

}

trait UserService extends BaseIdService[UserId, User] with UserQueries {
  object UserService extends UserService

  def saveUser(user: User)(implicit session: Session)  = UserService save user // save method comes from slick

  def getUserById(id: UserId)(implicit session: Session): Option[User] =
    UserService findById id

  def getUserByEmail(email: String)(implicit session: Session): Option[User] = {
    val user = ( UserService findByEmail email).firstOption
    user
  }
}
