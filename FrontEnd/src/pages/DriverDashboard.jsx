import React, { useEffect, useRef, useState } from 'react'
import { Typography, Grid, Card, CardContent, Chip, Button, CircularProgress, Box } from '@mui/material'
import api from '../api'
import SectionHeader from '../components/SectionHeader'

export default function DriverDashboard(){
  const [user] = useState(() => JSON.parse(localStorage.getItem('user')||'null'))
  const [available, setAvailable] = useState([])
  const [loading, setLoading] = useState(true)
  const fetchRef = useRef(false)

  useEffect(() => {
    if (fetchRef.current) return
    fetchRef.current = true

    if (!user?.id) {
      setAvailable([])
      setLoading(false)
      return
    }

    setLoading(true)
    api.get(`/drivers/available-bookings/${user.id}`)
      .then(r => setAvailable(r.data))
      .catch(() => setAvailable([]))
      .finally(() => setLoading(false))
  }, [user?.id])

  if (!user) return <Typography>Please login</Typography>
  if (loading) return <CircularProgress sx={{ display: 'block', mx: 'auto', mt: 8 }} />

  return (
    <Box>
      <SectionHeader title="Ride Requests" subtitle="Review closest available bookings and accept the ones you want." />
      <Grid container spacing={3}>
        {available.length === 0 ? (
          <Grid item xs={12}>
            <Typography color="text.secondary">No open bookings at the moment. Check again soon.</Typography>
          </Grid>
        ) : available.map((booking) => (
          <Grid item xs={12} md={6} key={booking.tripBookingId}>
            <Card sx={{ borderRadius: 3, boxShadow: 2, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
              <CardContent>
                <Typography variant="h6" gutterBottom>{booking.fromLocation} → {booking.toLocation}</Typography>
                <Chip label={`Customer: ${booking.customer?.username || 'Unknown'}`} size="small" sx={{ mb: 1 }} />
                <Typography variant="body2" color="text.secondary">Distance: {booking.distanceInKm || 'N/A'} km</Typography>
                <Typography variant="body2" color="text.secondary">Requested for: {booking.dateTime || 'TBA'}</Typography>
              </CardContent>
              <Box sx={{ p: 2 }}>
                <Button variant="contained" fullWidth>Take ride</Button>
              </Box>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  )
}
