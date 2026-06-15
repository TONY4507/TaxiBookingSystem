import React from 'react'
import { AppBar, Toolbar, Typography, Button, Box, IconButton, useMediaQuery, Drawer, List, ListItemButton, ListItemIcon, ListItemText } from '@mui/material'
import MenuIcon from '@mui/icons-material/Menu'
import HomeIcon from '@mui/icons-material/Home'
import LocalTaxiIcon from '@mui/icons-material/LocalTaxi'
import LoginIcon from '@mui/icons-material/Login'
import AccountCircleIcon from '@mui/icons-material/AccountCircle'
import { Link, useNavigate } from 'react-router-dom'

const navItems = [
  { label: 'Home', path: '/home' },
  { label: 'Cabs', path: '/cabs' },
  { label: 'Book Ride', path: '/book' },
]

export default function NavBar(){
  const navigate = useNavigate()
  const [drawerOpen, setDrawerOpen] = React.useState(false)
  const user = JSON.parse(localStorage.getItem('user')||'null')
  const mobile = useMediaQuery(theme => theme.breakpoints.down('md'))

  const logout = () => {
    localStorage.removeItem('user')
    navigate('/login')
  }

  const menuItems = [
    { label: 'Home', path: '/home', icon: <HomeIcon fontSize="small" /> },
    { label: 'Cabs', path: '/cabs', icon: <LocalTaxiIcon fontSize="small" /> },
    { label: 'Book Ride', path: '/book', icon: <LocalTaxiIcon fontSize="small" /> },
    ...(user ? [{ label: user.role === 'ADMIN' ? 'Admin' : user.role === 'DRIVER' ? 'Driver' : 'Profile', path: user.role === 'ADMIN' ? '/admin' : user.role === 'DRIVER' ? '/driver' : '/customer', icon: <AccountCircleIcon fontSize="small" /> }] : []),
  ]

  const drawer = (
    <Box sx={{ width: 250, p: 1 }} role="presentation" onClick={() => setDrawerOpen(false)}>
      <List>
        {menuItems.map(item => (
          <ListItemButton key={item.label} component={Link} to={item.path}>
            {item.icon && <ListItemIcon sx={{ minWidth: 38 }}>{item.icon}</ListItemIcon>}
            <ListItemText primary={item.label} />
          </ListItemButton>
        ))}
        {!user ? (
          <>
            <ListItemButton component={Link} to="/login"><ListItemText primary="Login" /></ListItemButton>
            <ListItemButton component={Link} to="/signup/customer"><ListItemText primary="Sign Up" /></ListItemButton>
          </>
        ) : (
          <ListItemButton onClick={logout}><ListItemText primary="Logout" /></ListItemButton>
        )}
      </List>
    </Box>
  )

  return (
    <AppBar position="sticky" elevation={4} color="primary">
      <Toolbar sx={{ justifyContent: 'space-between' }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <LocalTaxiIcon sx={{ fontSize: 32 }} />
          <Typography variant="h6" component={Link} to="/" sx={{ textDecoration: 'none', color: 'inherit', fontWeight: 700 }}>
            TaxiBook
          </Typography>
        </Box>
        {!mobile && (
          <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
            {menuItems.map(item => (
              <Button key={item.label} color="inherit" component={Link} to={item.path} startIcon={item.icon}>
                {item.label}
              </Button>
            ))}
          </Box>
        )}
        {mobile ? (
          <IconButton edge="end" color="inherit" onClick={() => setDrawerOpen(true)}>
            <MenuIcon />
          </IconButton>
        ) : (
          <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
            {!user && <Button color="inherit" component={Link} to="/login" startIcon={<LoginIcon />}>Login</Button>}
            {!user && <Button color="secondary" variant="contained" component={Link} to="/signup/customer">Sign Up</Button>}
            {user && <Button color="inherit" onClick={logout}>Logout</Button>}
          </Box>
        )}
      </Toolbar>
      <Drawer anchor="right" open={drawerOpen} onClose={() => setDrawerOpen(false)}>
        {drawer}
      </Drawer>
    </AppBar>
  )
}
