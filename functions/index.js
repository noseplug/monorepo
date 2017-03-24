const functions = require('firebase-functions')
const admin = require('firebase-admin')

admin.initializeApp(functions.config().firebase)

exports.notifyGlobalNewOdorEvent = functions.database.ref('/events/{id}').onWrite(event => {
  const ref = event.data.ref

  const odorEventName = ref.name.val()
  console.log('Odor Event Name: ' + odorEventName)
  const odorEventOwner = ref['owner-id'].val()

  const payload = {
    notification: {
      title: 'New Odor Event: ' + odorEventName,
      body: odorEventOwner + ' reported ' + odorEventName
    }
  }

  admin.messaging().sendToTopic('newOdorEvents', payload)

  return null
})
