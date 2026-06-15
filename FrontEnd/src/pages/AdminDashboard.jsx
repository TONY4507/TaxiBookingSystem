import React, { useEffect, useRef, useState } from 'react'
import { Typography, Grid, Card, CardContent, Button, Box, Stack, CircularProgress } from '@mui/material'
import api from '../api'
import SectionHeader from '../components/SectionHeader'

export default function AdminDashboard(){
  const [pendingCustomers, setPendingCustomers] = useState([])
  const [pendingDrivers, setPendingDrivers] = useState([])
  const [loading, setLoading] = useState(true)
  const fetchRef = useRef(false)

  useEffect(() => {
    if (fetchRef.current) return
    fetchRef.current = true

    setLoading(true)
    Promise.all([
      api.get('/admin/pending/customers'),
      api.get('/admin/pending/drivers'),
    ])
      .then(([customers, drivers]) => {
        setPendingCustomers(customers.data)
        setPendingDrivers(drivers.data)
      })
      .catch(() => {
        setPendingCustomers([])
        setPendingDrivers([])
      })
      .finally(() => setLoading(false))
  }, [])

  const action = async (type, id, approve) => {
    const path = `/admin/${approve ? 'approve' : 'reject'}/${type}/${id}`
    try {
      await api.put(path)
      if (type === 'customer') setPendingCustomers(prev => prev.filter(item => item.customerId !== id))
      else setPendingDrivers(prev => prev.filter(item => item.driverId !== id))
    } catch (e) {
      alert(e.response?.data || e.message)
    }
  }

  if (loading) return <CircularProgress sx={{ display: 'block', mx: 'auto', mt: 8 }} />

  return (
    <Box>
      <SectionHeader title="Admin Panel" subtitle="Manage customer and driver approvals in one place." />
      <Typography variant="h6" sx={{ mb: 2 }}>Pending Customers</Typography>
      <Grid container spacing={3} sx={{ mb: 4 }}>
        {pendingCustomers.length === 0 ? (
          <Grid item xs={12}><Typography color="text.secondary">No pending customer approvals.</Typography></Grid>
        ) : pendingCustomers.map(c => (
          <Grid item xs={12} sm={6} md={4} key={c.customerId}>
            <Card sx={{ borderRadius: 3, boxShadow: 2 }}>
              <CardContent>
                <Typography variant="h6">{c.username}</Typography>
                <Typography variant="body2" color="text.secondary">{c.email}</Typography>
                <Stack direction="row" spacing={1} sx={{ mt: 2 }}>
                  <Button size="small" variant="contained" onClick={() => action('customer', c.customerId, true)}>
                    Approve
                  </Button>
                  <Button size="small" variant="outlined" color="error" onClick={() => action('customer', c.customerId, false)}>
                    Reject
                  </Button>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Typography variant="h6" sx={{ mb: 2 }}>Pending Drivers</Typography>
      <Grid container spacing={3}>
        {pendingDrivers.length === 0 ? (
          <Grid item xs={12}><Typography color="text.secondary">No pending driver approvals.</Typography></Grid>
        ) : pendingDrivers.map(d => (
          <Grid item xs={12} sm={6} md={4} key={d.driverId}>
            <Card sx={{ borderRadius: 3, boxShadow: 2 }}>
              <CardContent>
                <Typography variant="h6">{d.username}</Typography>
                <Typography variant="body2" color="text.secondary">{d.email}</Typography>
                <Stack direction="row" spacing={1} sx={{ mt: 2 }}>
                  <Button size="small" variant="contained" onClick={() => action('driver', d.driverId, true)}>
                    Approve
                  </Button>
                  <Button size="small" variant="outlined" color="error" onClick={() => action('driver', d.driverId, false)}>
                    Reject
                  </Button>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  )
}
