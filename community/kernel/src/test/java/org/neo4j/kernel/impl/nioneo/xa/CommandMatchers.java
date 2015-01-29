/**
 * Copyright (c) 2002-2015 "Neo Technology,"
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
package org.neo4j.kernel.impl.nioneo.xa;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import org.neo4j.kernel.impl.transaction.xaframework.LogEntry;

public class CommandMatchers
{
    public static Matcher<? extends LogEntry> nodeCommandEntry( final int identifier, final int nodeId)
    {
        return new TypeSafeMatcher<LogEntry.Command>() {

            @Override
            public boolean matchesSafely( LogEntry.Command entry )
            {
                if( entry != null
                        && entry.getIdentifier() == identifier
                        && entry.getXaCommand() != null
                        && entry.getXaCommand() instanceof org.neo4j.kernel.impl.nioneo.xa.command.Command.NodeCommand)
                {
                    org.neo4j.kernel.impl.nioneo.xa.command.Command.NodeCommand cmd = (org.neo4j.kernel.impl.nioneo.xa.command.Command.NodeCommand) entry.getXaCommand();

                    return cmd.getKey() == nodeId;
                }

                return false;
            }

            @Override
            public void describeTo( Description description )
            {
                description.appendText( String.format( "Command[%d, Node[%d,used=<Any boolean>,rel=<Any relchain>,prop=<Any relchain>]]",
                        identifier, nodeId ) );
            }
        };
    }

}