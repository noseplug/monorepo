const functions = require('firebase-functions')
const admin = require('firebase-admin')

admin.initializeApp(functions.config().firebase)

exports.notifyGlobalNewOdorEvent = functions.database.ref('/events/{id}').onWrite(event => {
  if (!event.data.exists() || event.data.previous.exists()) {
    // TODO: Check if this is inefficient because it's triggered on
    // every write to every event. There might be a way to listen only
    // to new events by changing the path of the database ref above.
    return null
  }

  var odorEventName = event.data.child('name').val()
  var odorEventOwner = event.data.child('owner-id').val()

  var payload = {
    notification: {
      title: 'New Odor Event: ' + odorEventName,
      body: odorEventOwner + ' reported ' + odorEventName
    }
  }

  admin.messaging().sendToTopic('newOdorEvents', payload)

  return null
})
