/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.edurt.datacap.parser.tree;

import com.google.common.collect.ImmutableList;
import io.edurt.datacap.parser.ast.AstVisitor;
import io.edurt.datacap.parser.node.Node;
import io.edurt.datacap.parser.node.NodeLocation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CurrentUser
        extends Expression
{
    public CurrentUser()
    {
        this(Optional.empty());
    }

    public CurrentUser(NodeLocation location)
    {
        this(Optional.of(location));
    }

    private CurrentUser(Optional<NodeLocation> location)
    {
        super(location);
    }

    @Override
    public List<Node> getChildren()
    {
        return ImmutableList.of();
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context)
    {
        return visitor.visitCurrentUser(this, context);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        return (obj != null) && (getClass() == obj.getClass());
    }

    @Override
    public boolean shallowEquals(Node other)
    {
        return sameClass(this, other);
    }
}
