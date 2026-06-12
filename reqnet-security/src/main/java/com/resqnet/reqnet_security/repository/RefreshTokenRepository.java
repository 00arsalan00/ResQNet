package com.resqnet.reqnet_security.repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<User, UUID> {

}