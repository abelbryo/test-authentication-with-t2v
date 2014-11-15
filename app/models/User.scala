package models

import org.joda.time.DateTime

import scala.slick.session.Session
import org.virtuslab.unicorn.ids._

import authentication.Permission

case class UserId(id: Long) extends AnyVal with BaseId

object UserId extends IdCompanion[UserId]

// User entity class

case class User(
  id: Option[UserId],
  firstName: String,
  lastName: String,
  username: String,
  password: String,
  email: String,
  phoneNumber: String,
  permission: String,
  created: DateTime) extends WithId[UserId]

object Users extends IdTable[UserId, User]("USERS") {
  def firstName   = column[String]("FIRST_NAME", O.NotNull)
  def lastName    = column[String]("LAST_NAME", O.NotNull)
  def username    = column[String]("USERNAME", O.NotNull)
  def password    = column[String]("PASSWORD", O.NotNull)
  def email       = column[String]("EMAIL", O.NotNull)
  def phoneNumber = column[String]("PHONE_NUMBER", O.NotNull)
  def permission = column[String]("PERMISSION", O.NotNull)
  def created     = column[DateTime]("CREATED_ON", O.NotNull)

  def uniqueUsername = index("IDX_USER_USERNAME", username, unique=true)
  def uniqueEmail = index("IDX_USER_EMAIL", email, unique = true)

  def base = firstName ~ lastName ~ username ~ password ~ email ~ phoneNumber ~ permission ~ created

  override def * = id.? ~: base <> (User.apply _, User.unapply _)

  override def insertOne(elem: User)(implicit session: Session): UserId =
    saveBase(base, User.unapply _)(elem)
}


// This is a class that will be passed to a Form object
// when creating a User sign up form
case class UserFormWrapper(
  firstName: String,
  lastName: String,
  username: String,
  password: String,
  email: String,
  phoneNumber: String)

case class LoginForm(email: String, password: String)

