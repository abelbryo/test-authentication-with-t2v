package dao

import play.api.db.slick._
import play.api.Play.current
import scala.slick.session.Session

import org.joda.time.DateTime
import org.mindrot.jbcrypt.BCrypt._

import models._
import services._

object UserDAO {
  object UserService extends UserService

  def saveUserInDB(user: User) = DB.withTransaction {
    implicit session: Session =>
      UserService.saveUser(user)
  }

  // get user by id
  def getUserById(id: UserId): Option[User] = DB.withSession {
    implicit session: Session =>
      UserService.getUserById(id)
  }

  def getUserByEmail(email: String): Option[User] = DB.withSession {
    implicit session: Session =>
      UserService.getUserByEmail(email)
  }

}
