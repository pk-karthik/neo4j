/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.server.security.auth;

import org.apache.shiro.subject.Subject;

import java.io.IOException;

import org.neo4j.kernel.api.security.AccessMode;
import org.neo4j.kernel.api.security.AuthSubject;
import org.neo4j.kernel.api.security.AuthenticationResult;
import org.neo4j.kernel.api.security.exception.IllegalCredentialsException;

public class ShiroAuthSubject implements AuthSubject
{
    private final ShiroAuthManager authManager;
    private final Subject subject;
    private final AuthenticationResult authenticationResult;

    public ShiroAuthSubject( ShiroAuthManager authManager, Subject subject, AuthenticationResult authenticationResult )
    {
        this.authManager = authManager;
        this.subject = subject;
        this.authenticationResult = authenticationResult;
    }

    @Override
    public void logout()
    {
        subject.logout();
    }

    @Override
    public AuthenticationResult getAuthenticationResult() { return authenticationResult; }

    @Override
    public void setPassword( String password ) throws IOException, IllegalCredentialsException
    {
        authManager.setPassword( this, (String) subject.getPrincipals().getPrimaryPrincipal(), password );
    }

    @Override
    public boolean allowsReads()
    {
        return getAccesMode().allowsReads();
    }

    @Override
    public boolean allowsWrites()
    {
        return getAccesMode().allowsWrites();
    }

    @Override
    public boolean allowsSchemaWrites()
    {
        return getAccesMode().allowsSchemaWrites();
    }

    @Override
    public String name()
    {
        return "AUTH";
    }

    private AccessMode.Static getAccesMode()
    {
        if ( subject.isAuthenticated() )
        {
            if ( subject.hasRole( "schema" ) )
            {
                return AccessMode.Static.FULL;
            }
            else if ( subject.hasRole( "write" ) )
            {
                return AccessMode.Static.WRITE;
            }
            else if ( subject.hasRole( "read" ) )
            {
                return AccessMode.Static.READ;
            }
        }
        return AccessMode.Static.NONE;
    }
}
