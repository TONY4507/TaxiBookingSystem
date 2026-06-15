import React from 'react'
import { Box, Button, Grid, Card, CardContent, Typography, Stack, Paper } from '@mui/material'
import LocalTaxiIcon from '@mui/icons-material/LocalTaxi'
import MapIcon from '@mui/icons-material/Map'
import PaymentIcon from '@mui/icons-material/Payment'
import StarRateIcon from '@mui/icons-material/StarRate'
import { Link as RouterLink } from 'react-router-dom'

const featureItems = [
  {
    icon: <LocalTaxiIcon fontSize="large" color="primary" />,
    title: 'Choose a ride instantly',
    description: 'Search from our available cabs and book a vehicle with a single tap.',
  },
  {
    icon: <MapIcon fontSize="large" color="primary" />,
    title: 'Real-time tracking',
    description: 'Follow your cab on the map and receive live updates from pickup to drop-off.',
  },
  {
    icon: <PaymentIcon fontSize="large" color="primary" />,
    title: 'Secure payments',
    description: 'Pay safely using multiple payment options with end-to-end transaction security.',
  },
  {
    icon: <StarRateIcon fontSize="large" color="primary" />,
    title: 'Rate every ride',
    description: 'Give feedback on drivers and help us keep service quality high.',
  },
]

export default function Home() {
  return (
    <Box>
      <Paper sx={{ p: { xs: 4, md: 6 }, mb: 4, borderRadius: 4, background: 'linear-gradient(135deg, rgba(11,108,247,0.12), rgba(255,179,0,0.12))' }}>
        <Grid container spacing={4} alignItems="center">
          <Grid item xs={12} md={7}>
            <Typography variant="h3" component="h1" fontWeight={800} gutterBottom>
              Book taxis faster with a modern ride experience
            </Typography>
            <Typography variant="h6" color="text.secondary" sx={{ mb: 3, maxWidth: 560 }}>
              Clean, intuitive booking for customers. Track your cab in real time, estimate fares instantly, and pay securely.
            </Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <Button component={RouterLink} to="/cabs" variant="contained" size="large">
                Browse cabs
              </Button>
              <Button component={RouterLink} to="/book" variant="outlined" size="large">
                Start booking
              </Button>
            </Stack>
          </Grid>
          <Grid item xs={12} md={5}>
            <Box sx={{ display: 'grid', gap: 2 }}>
              {featureItems.slice(0, 2).map((item) => (
                <Card key={item.title} sx={{ p: 3, borderRadius: 3, boxShadow: 4, backgroundColor: '#fff' }}>
                  <Stack direction="row" spacing={2} alignItems="center">
                    {item.icon}
                    <Box>
                      <Typography variant="subtitle1" fontWeight={700}>
                        {item.title}
                      </Typography>
                      <Typography color="text.secondary" variant="body2">
                        {item.description}
                      </Typography>
                    </Box>
                  </Stack>
                </Card>
              ))}
            </Box>
          </Grid>
        </Grid>
      </Paper>

      <Typography variant="h5" fontWeight={700} gutterBottom>
        Designed for effortless taxi booking
      </Typography>
      <Grid container spacing={3}>
        {featureItems.map((item) => (
          <Grid item xs={12} sm={6} md={3} key={item.title}>
            <Card sx={{ minHeight: 200, borderRadius: 3, boxShadow: 3 }}>
              <CardContent>
                <Stack spacing={2} alignItems="flex-start">
                  {item.icon}
                  <Typography variant="h6" fontWeight={700}>
                    {item.title}
                  </Typography>
                  <Typography color="text.secondary" variant="body2">
                    {item.description}
                  </Typography>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  )
}
