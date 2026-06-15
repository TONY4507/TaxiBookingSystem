import React, { useMemo, useState } from 'react'
import { Paper, Box, TextField, Button, Typography, Grid, Alert, Stack, MenuItem, Chip, LinearProgress, Divider } from '@mui/material'
import api from '../api'
import { useLocation } from 'react-router-dom'
import dayjs from 'dayjs'
import SectionHeader from '../components/SectionHeader'

const paymentMethods = [
  { value: 'card', label: 'Card payment' },
  { value: 'upi', label: 'UPI / Wallet' },
  { value: 'cash', label: 'Cash on delivery' },
]

export default function Booking(){
  const loc = useLocation()
  const cab = loc.state?.cab
  const user = JSON.parse(localStorage.getItem('user')||'null')
  const [form, setForm] = useState({
    fromLocation: '',
    toLocation: '',
    dateTime: dayjs().format('YYYY-MM-DDTHH:mm'),
    distanceInKm: 1,
    paymentMethod: 'card',
    promoCode: '',
    cardNumber: '',
    expiry: '',
    cvv: '',
  })
  const [result, setResult] = useState(null)
  const [bookingConfirmed, setBookingConfirmed] = useState(false)
  const [submitting, setSubmitting] = useState(false)

  const ratePerKm = cab?.perKmCharge || 18
  const baseFare = 40
  const rawFare = Math.max(0, form.distanceInKm * ratePerKm + baseFare)
  const discount = form.promoCode.trim().toUpperCase() === 'TAXI10' ? rawFare * 0.12 : 0
  const estimatedFare = rawFare - discount
  const eta = Math.max(4, Math.round(form.distanceInKm * 2.5))

  const submit = async () => {
    if (!user) return setResult({ type: 'error', message: 'Please login first' })
    if (!form.fromLocation || !form.toLocation) return setResult({ type: 'error', message: 'Please enter both pickup and drop-off locations.' })
    if (form.paymentMethod === 'card' && (!form.cardNumber || !form.expiry || !form.cvv)) {
      return setResult({ type: 'error', message: 'Please enter your card details or choose another payment method.' })
    }

    try {
      setSubmitting(true)
      const params = {
        customerId: user.id || 1,
        cabid: cab?.cabId || form.cabId || '',
        fromLocation: form.fromLocation,
        toLocation: form.toLocation,
        DateTime: form.dateTime,
        distanceInKm: form.distanceInKm,
        paymentMethod: form.paymentMethod,
        fare: estimatedFare,
      }

      await api.post('/customers/bookings', null, { params })
      setResult({ type: 'success', message: 'Ride booked successfully. Track your driver in real time from your dashboard.' })
      setBookingConfirmed(true)
    } catch (e) {
      setResult({ type: 'error', message: e.response?.data || e.message })
    } finally {
      setSubmitting(false)
    }
  }

  const trackStatus = useMemo(() => {
    if (!bookingConfirmed) return 'Waiting for confirmation'
    return 'Driver is on the way'
  }, [bookingConfirmed])

  return (
    <Paper sx={{ p: 4, maxWidth: 920, mx: 'auto', borderRadius: 4, boxShadow: 3, backgroundColor: '#fff' }}>
      <SectionHeader
        title="Book your next ride"
        subtitle={cab ? `Selected cab: ${cab.model || cab.cabId}` : 'Fill in route details and get a fare estimate instantly.'}
      />

      <Grid container spacing={4}>
        <Grid item xs={12} lg={7}>
          <Stack spacing={3}>
            <Box sx={{ display: 'grid', gap: 2 }}>
              <TextField
                label="Pickup location"
                value={form.fromLocation}
                onChange={e => setForm({ ...form, fromLocation: e.target.value })}
                fullWidth
                placeholder="Enter pickup address"
              />
              <TextField
                label="Drop-off location"
                value={form.toLocation}
                onChange={e => setForm({ ...form, toLocation: e.target.value })}
                fullWidth
                placeholder="Enter drop-off address"
              />
              <TextField
                label="Pickup time"
                type="datetime-local"
                value={form.dateTime}
                onChange={e => setForm({ ...form, dateTime: e.target.value })}
                InputLabelProps={{ shrink: true }}
                fullWidth
              />
              <TextField
                label="Distance (km)"
                type="number"
                value={form.distanceInKm}
                onChange={e => setForm({ ...form, distanceInKm: Math.max(1, Number(e.target.value)) })}
                fullWidth
              />
            </Box>

            <Divider />

            <Box sx={{ display: 'grid', gap: 2 }}>
              <TextField
                select
                label="Payment method"
                value={form.paymentMethod}
                onChange={e => setForm({ ...form, paymentMethod: e.target.value })}
                fullWidth
              >
                {paymentMethods.map((option) => (
                  <MenuItem key={option.value} value={option.value}>
                    {option.label}
                  </MenuItem>
                ))}
              </TextField>

              {form.paymentMethod === 'card' && (
                <Grid container spacing={2}>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      label="Card number"
                      value={form.cardNumber}
                      onChange={e => setForm({ ...form, cardNumber: e.target.value })}
                      fullWidth
                      placeholder="XXXX XXXX XXXX XXXX"
                    />
                  </Grid>
                  <Grid item xs={6} sm={3}>
                    <TextField
                      label="Expiry"
                      value={form.expiry}
                      onChange={e => setForm({ ...form, expiry: e.target.value })}
                      fullWidth
                      placeholder="MM/YY"
                    />
                  </Grid>
                  <Grid item xs={6} sm={3}>
                    <TextField
                      label="CVV"
                      type="password"
                      value={form.cvv}
                      onChange={e => setForm({ ...form, cvv: e.target.value })}
                      fullWidth
                      placeholder="123"
                    />
                  </Grid>
                </Grid>
              )}

              <TextField
                label="Promo code"
                value={form.promoCode}
                onChange={e => setForm({ ...form, promoCode: e.target.value })}
                fullWidth
              />
            </Box>

            <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mt: 2 }}>
              <Typography variant="body2" color="text.secondary">
                Estimate based on distance and current cab fare.
              </Typography>
              <Chip label={form.promoCode.trim().toUpperCase() === 'TAXI10' ? 'Promo applied' : 'No promo'} color={form.promoCode.trim().toUpperCase() === 'TAXI10' ? 'success' : 'default'} />
            </Stack>

            <Button variant="contained" size="large" onClick={submit} fullWidth disabled={submitting}>
              {submitting ? 'Booking...' : 'Confirm booking & pay'}
            </Button>
          </Stack>
        </Grid>

        <Grid item xs={12} lg={5}>
          <Stack spacing={3}>
            <Box sx={{ p: 3, borderRadius: 4, boxShadow: 2, backgroundColor: '#f7fbff' }}>
              <Typography variant="subtitle1" fontWeight={700} sx={{ mb: 1 }}>
                Fare estimate
              </Typography>
              <Typography variant="h4" fontWeight={800} sx={{ mb: 1 }}>
                ₹{estimatedFare.toFixed(0)}
              </Typography>
              <Typography color="text.secondary">Base fare ₹{baseFare}</Typography>
              <Typography color="text.secondary">Rate per km ₹{ratePerKm}</Typography>
              {discount > 0 && (
                <Typography color="success.main">Discount ₹{discount.toFixed(0)} applied</Typography>
              )}
              <Typography color="text.secondary" sx={{ mt: 2 }}>
                Estimated arrival in {eta} minutes after booking confirmation.
              </Typography>
            </Box>

            <Box sx={{ p: 3, borderRadius: 4, boxShadow: 2, backgroundColor: '#fff' }}>
              <Typography variant="subtitle1" fontWeight={700} sx={{ mb: 1 }}>
                Trip details
              </Typography>
              <Typography><strong>From:</strong> {form.fromLocation || 'Pending'}</Typography>
              <Typography><strong>To:</strong> {form.toLocation || 'Pending'}</Typography>
              <Typography><strong>Date:</strong> {dayjs(form.dateTime).format('MMM D, YYYY h:mm A')}</Typography>
              <Typography><strong>Cab:</strong> {cab?.model || 'Best available'}</Typography>
              <Typography><strong>Status:</strong> {bookingConfirmed ? 'Confirmed' : 'Waiting'}</Typography>
            </Box>

            <Box sx={{ p: 3, borderRadius: 4, boxShadow: 2, backgroundColor: '#f7fbff' }}>
              <Typography variant="subtitle1" fontWeight={700} sx={{ mb: 1 }}>
                Real-time tracking
              </Typography>
              <Typography color="text.secondary" sx={{ mb: 2 }}>
                {trackStatus}
              </Typography>
              <LinearProgress variant={bookingConfirmed ? 'determinate' : 'indeterminate'} value={bookingConfirmed ? 75 : 0} sx={{ height: 10, borderRadius: 5 }} />
              {bookingConfirmed && (
                <Typography variant="body2" sx={{ mt: 1 }}>
                  Driver is 2.4 km away. Estimated pickup in {eta} mins.
                </Typography>
              )}
            </Box>
          </Stack>
        </Grid>
      </Grid>

      {result && (
        <Alert severity={result.type} sx={{ mt: 4 }}>
          {result.message}
        </Alert>
      )}
    </Paper>
  )
}
