package de.fsmpi.model.user;

import de.fsmpi.misc.Cart;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
public class User implements UserDetails {

    @Id
    @Column
    private String username;

    @Column
    private String password;

    @Column
    private Boolean enabled;

    @Column
	private String firstName;

    @Column
	private String lastName;

    @ManyToOne
	@JoinColumn
	private Cart cart;

    @Column
    @CollectionTable
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = UserAuthority.class, fetch = FetchType.EAGER)
    private Set<UserAuthority> userAuthorities;

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<UserAuthority> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(Set<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Transient
	public String getFullName() {
    	String fullName = "";
    	if(firstName != null && firstName.trim().length() > 0) {
    		fullName += firstName.trim();
    		fullName += ' ';
		}
		if(lastName != null && lastName.trim().length() > 0) {
			fullName += lastName.trim();
		}
		if(fullName.length() == 0) {
			return username;
    	}
    	return fullName;
	}

	@Transient
    public boolean isAdmin() {
        return this.userAuthorities != null && this.getUserAuthorities().contains(UserAuthority.EDIT_OPTIONS);
    }

    @Transient
    public boolean canPrint() {
        return userAuthorities != null && userAuthorities.contains(UserAuthority.DO_PRINT);
    }

    @Transient
	public boolean canViewPDFs() {
    	return userAuthorities != null && userAuthorities.contains(UserAuthority.VIEW_PDF);
	}
}
