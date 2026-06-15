import React, { useEffect, useMemo, useRef, useState } from 'react'
import { Typography, Grid, Card, CardContent, Chip, CircularProgress, Box, Stack, Button, TextField } from '@mui/material'
import PersonOutlineIcon from '@mui/icons-material/PersonOutline'
import EmailIcon from '@mui/icons-material/Email'
import PhoneIcon from '@mui/icons-material/Phone'
import HomeIcon from '@mui/icons-material/Home'
import PublicIcon from '@mui/icons-material/Public'
import LocationOnIcon from '@mui/icons-material/LocationOn'
import CakeIcon from '@mui/icons-material/Cake'
import api from '../api'
import SectionHeader from '../components/SectionHeader'
import { useNavigate } from 'react-router-dom'

export default function CustomerDashboard(){
  const [user] = useState(() => JSON.parse(localStorage.getItem('user')||'null'))
  const [bookings, setBookings] = useState([])
  const [loading, setLoading] = useState(true)
  const [reviews, setReviews] = useState(() => JSON.parse(localStorage.getItem('driverReviews') || '{}'))
  const [feedback, setFeedback] = useState({})
  const navigate = useNavigate()

  const fetchRef = useRef(false)

  useEffect(() => {
    if (fetchRef.current) return
    fetchRef.current = true

    if (!user?.id) {
      setBookings([])
      setLoading(false)
      return
    }

    setLoading(true)
    api.get(`/customers/${user.id}/bookings`)
      .then(r => setBookings(r.data))
      .catch(() => setBookings([]))
      .finally(() => setLoading(false))
  }, [user?.id])

  const updateReview = (bookingId, field, value) => {
    setFeedback(prev => ({ ...prev, [bookingId]: { ...prev[bookingId], [field]: value } }))
  }

  const submitReview = (booking) => {
    const rating = Number(feedback[booking.tripBookingId]?.rating || 5)
    const comment = feedback[booking.tripBookingId]?.comment || ''
    const next = { ...reviews, [booking.tripBookingId]: { rating, comment, driver: booking.driver?.username || 'Driver' } }
    setReviews(next)
    localStorage.setItem('driverReviews', JSON.stringify(next))
  }

  const confirmedRides = useMemo(() => bookings.filter(booking => booking.status === 'CONFIRMED' || booking.status === 'COMPLETED'), [bookings])

  if (!user) return <Typography>Please login to view your dashboard.</Typography>
  if (loading) return <CircularProgress sx={{ display: 'block', mx: 'auto', mt: 8 }} />

  return (
    <Box>
      <SectionHeader title={`Welcome back, ${user.username || user.name || 'Customer'}`} subtitle="Track your trips, payments, and driver reviews in one place." />

      <Card sx={{ p: 2, mb: 3, borderRadius: 2 }}>
        <Typography variant="subtitle1" fontWeight={700} gutterBottom>Profile</Typography>
        <Stack spacing={1}>
          <Stack direction="row" spacing={1} alignItems="center">
            <PersonOutlineIcon fontSize="small" />
            <Typography>Name: {user.username || user.name || 'N/A'}</Typography>
          </Stack>
          <Stack direction="row" spacing={1} alignItems="center">
            <EmailIcon fontSize="small" />
            <Typography>Email: {user.email || 'N/A'}</Typography>
          </Stack>
          <Stack direction="row" spacing={1} alignItems="center">
            <PhoneIcon fontSize="small" />
            <Typography>Phone: {user.mobileNumber || user.mobile || 'N/A'}</Typography>
          </Stack>
          <Stack direction="row" spacing={1} alignItems="center">
            <HomeIcon fontSize="small" />
            <Typography>Address: {user.address || 'N/A'}</Typography>
          </Stack>
          <Stack direction="row" spacing={1} alignItems="center">
            <LocationOnIcon fontSize="small" />
            <Typography>City: {user.city || 'N/A'}</Typography>
          </Stack>
          <Stack direction="row" spacing={1} alignItems="center">
            <PublicIcon fontSize="small" />
            <Typography>Country: {user.country || 'N/A'}</Typography>
          </Stack>
          <Stack direction="row" spacing={1} alignItems="center">
            <LocationOnIcon fontSize="small" />
            <Typography>Postal Code: {user.postalCode || user.postal || 'N/A'}</Typography>
          </Stack>
          <Stack direction="row" spacing={1} alignItems="center">
            <CakeIcon fontSize="small" />
            <Typography>Date of Birth: {user.dateOfBirth || user.dob || 'N/A'}</Typography>
          </Stack>
        </Stack>
      </Card>

      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} md={6}>
          <Card sx={{ p: 3, borderRadius: 3, boxShadow: 3 }}>
            <Typography variant="subtitle1" fontWeight={700} gutterBottom>Ride summary</Typography>
            <Typography>Total trips booked: {bookings.length}</Typography>
            <Typography>Pending confirmations: {bookings.filter(booking => booking.status === 'PENDING').length}</Typography>
            <Typography>Completed trips: {bookings.filter(booking => booking.status === 'COMPLETED').length}</Typography>
          </Card>
        </Grid>
        <Grid item xs={12} md={6}>
          <Card sx={{ p: 3, borderRadius: 3, boxShadow: 3 }}>
            <Typography variant="subtitle1" fontWeight={700} gutterBottom>Quick actions</Typography>
            <Stack spacing={2}>
              <Button variant="contained" onClick={() => navigate('/cabs')}>Browse cabs</Button>
              <Button variant="outlined" onClick={() => navigate('/book')}>Make a booking</Button>
            </Stack>
          </Card>
        </Grid>
      </Grid>

      {confirmedRides.length === 0 ? (
        <Typography color="text.secondary">No confirmed or completed rides found yet. Start by booking a new ride.</Typography>
      ) : (
        <Grid container spacing={3}>
          {confirmedRides.map((booking) => {
          const hasReview = reviews[booking.tripBookingId]
          const status = booking.status || 'Pending'
          return (
            <Grid item xs={12} md={6} key={booking.tripBookingId}>
              <Card sx={{ borderRadius: 3, boxShadow: 3 }}>
                <CardContent>
                  <Stack spacing={2}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', gap: 2, flexWrap: 'wrap' }}>
                      <Typography variant="h6">{booking.fromLocation} → {booking.toLocation}</Typography>
                      <Chip label={status} color={status === 'CONFIRMED' || status === 'COMPLETED' ? 'success' : 'warning'} />
                    </Box>
                    <Typography variant="body2" color="text.secondary">Driver: {booking.driver?.username || 'Unassigned'}</Typography>
                    <Typography variant="body2" color="text.secondary">Cab: {booking.cab?.model || booking.cabid || 'Standard'}</Typography>
                    <Typography variant="body2" color="text.secondary">Distance: {booking.distanceInKm || 'N/A'} km</Typography>
                    <Typography variant="body2" color="text.secondary">Booking ID: {booking.tripBookingId}</Typography>

                    {hasReview ? (
                      <Box sx={{ mt: 2, p: 2, borderRadius: 3, backgroundColor: '#f4f7ff' }}>
                        <Typography variant="subtitle2">Your review</Typography>
                        <Typography variant="body2">Rating: {hasReview.rating}/5</Typography>
                        <Typography variant="body2" color="text.secondary">{hasReview.comment}</Typography>
                      </Box>
                    ) : (
                      <Box sx={{ mt: 2, p: 2, borderRadius: 3, backgroundColor: '#f8fafc' }}>
                        <Typography variant="subtitle2" gutterBottom>Rate your driver</Typography>
                        <Stack spacing={2}>
                          <TextField
                            label="Rating (1-5)"
                            type="number"
                            inputProps={{ min: 1, max: 5 }}
                            value={feedback[booking.tripBookingId]?.rating || ''}
                            onChange={e => updateReview(booking.tripBookingId, 'rating', e.target.value)}
                            size="small"
                          />
                          <TextField
                            label="Feedback"
                            value={feedback[booking.tripBookingId]?.comment || ''}
                            onChange={e => updateReview(booking.tripBookingId, 'comment', e.target.value)}
                            size="small"
                            multiline
                            minRows={2}
                          />
                          <Button size="small" variant="contained" onClick={() => submitReview(booking)}>
                            Submit review
                          </Button>
                        </Stack>
                      </Box>
                    )}
                  </Stack>
                </CardContent>
              </Card>
            </Grid>
          )
        })}
      </Grid>    )}    </Box>
  )
}
