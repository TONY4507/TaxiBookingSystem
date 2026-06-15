import React, { useEffect, useMemo, useRef, useState } from 'react'
import { Grid, Card, CardContent, Typography, Button, CircularProgress, Chip, Stack, Box, TextField, MenuItem, Divider } from '@mui/material'
import api from '../api'
import { useNavigate } from 'react-router-dom'
import SectionHeader from '../components/SectionHeader'

export default function Cabs(){
  const [cabs, setCabs] = useState(null)
  const [filter, setFilter] = useState('available')
  const [searchTerm, setSearchTerm] = useState('')
  const nav = useNavigate()

  const fetchRef = useRef(false)

  useEffect(() => {
    if (fetchRef.current) return
    fetchRef.current = true

    api.get('/cabs/available').then(r => setCabs(r.data)).catch(() => setCabs([]))
  }, [])

  const filteredCabs = useMemo(() => {
    if (!cabs) return []
    return cabs.filter(cab => {
      if (filter === 'available' && !cab.available) return false
      if (searchTerm && !`${cab.model || ''} ${cab.driverName || ''} ${cab.regNumber || ''}`.toLowerCase().includes(searchTerm.toLowerCase())) return false
      return true
    })
  }, [cabs, filter, searchTerm])

  if (cabs === null) return <CircularProgress sx={{ display: 'block', mx: 'auto', mt: 8 }} />

  return (
    <Box>
      <SectionHeader title="Available Cabs" subtitle="Choose a comfortable ride from our modern taxi fleet." />
      <Box sx={{ display: 'grid', gap: 2, mb: 3, gridTemplateColumns: '1fr', '@media (min-width: 900px)': { gridTemplateColumns: '1.5fr 1fr' } }}>
        <TextField
          value={searchTerm}
          onChange={e => setSearchTerm(e.target.value)}
          placeholder="Search by cab, driver, or registration"
          fullWidth
        />
        <TextField select label="Filter" value={filter} onChange={e => setFilter(e.target.value)} fullWidth>
          <MenuItem value="available">Available now</MenuItem>
          <MenuItem value="all">Show all</MenuItem>
        </TextField>
      </Box>

      {filteredCabs.length === 0 && (
        <Typography variant="body1" color="text.secondary" sx={{ mb: 2 }}>
          No cabs match the current search and filter. Try changing the criteria.
        </Typography>
      )}

      <Grid container spacing={3}>
        {filteredCabs.map(cab => {
          const rating = cab.rating ?? 4.7
          const statusColor = cab.available ? 'success' : 'warning'
          return (
            <Grid item xs={12} sm={6} md={4} key={cab.cabId}>
              <Card sx={{ minHeight: 280, display: 'flex', flexDirection: 'column', justifyContent: 'space-between', borderRadius: 3, boxShadow: 3 }}>
                <CardContent>
                  <Stack spacing={1}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                      <Typography variant="h6" fontWeight={700}>{cab.model || cab.regNumber || 'Premium Cab'}</Typography>
                      <Chip label={cab.available ? 'Available' : 'Busy'} color={statusColor} size="small" />
                    </Box>
                    <Typography variant="body2" color="text.secondary">
                      {cab.driverName ? `Driven by ${cab.driverName}` : 'Experienced driver assigned soon'}
                    </Typography>
                    <Stack direction="row" spacing={1} alignItems="center" sx={{ flexWrap: 'wrap' }}>
                      <Chip label={`₹${cab.perKmCharge ?? 18}/km`} variant="outlined" size="small" />
                      <Chip label={`Rating ${rating.toFixed(1)}`} color="primary" size="small" />
                    </Stack>
                    <Divider sx={{ my: 1.5 }} />
                    <Typography variant="body2">Registration: {cab.regNumber || 'N/A'}</Typography>
                    <Typography variant="body2">Seats: {cab.seatingCapacity || 4} persons</Typography>
                    <Typography variant="body2">Style: {cab.category || 'Sedan'}</Typography>
                  </Stack>
                </CardContent>
                <Box sx={{ p: 2 }}>
                  <Button fullWidth variant="contained" disabled={!cab.available} onClick={() => nav('/book', { state: { cab } })}>
                    Book Now
                  </Button>
                </Box>
              </Card>
            </Grid>
          )
        })}
      </Grid>
    </Box>
  )
}
